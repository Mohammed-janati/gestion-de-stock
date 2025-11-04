resource "aws_instance" "frontend" {
  subnet_id = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.frontend.id]
  ami = var.frontAMI
  instance_type = var.frontinstanceType

  tags = {Name="stock app frontend"}

  user_data = <<-EOF
              #!/bin/bash
              apt-get update -y
              apt-get install -y docker.io
              systemctl start docker
              systemctl enable docker


              docker pull ${var.DOCKER_HUB_USER}/frontend:latest

              docker run -d --name frontend \
                -p 80:80 \
                -e API_URL="http://${aws_instance.backend.private_ip}:8080" \
                ${var.DOCKER_HUB_USER}/frontend:latest
              EOF


}


resource "aws_security_group" "frontend" {
  vpc_id = aws_vpc.vpc.id
  ingress {
    cidr_blocks = ["0.0.0.0/0"]
    from_port = 80
    to_port = 80
    protocol = "tcp"
  }
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # ⚠️ only for testing
  }

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

    #backend-----------------
resource "aws_instance" "backend" {
  subnet_id = aws_subnet.private_subnet.id
  vpc_security_group_ids = [aws_security_group.BACKEND.id]
  ami = var.backAMI
  instance_type = var.backinstanceType

  tags = {Name="stock app backend"}

  user_data = <<-EOF
            #!/bin/bash
            apt-get update -y
            apt-get install -y docker.io
            systemctl start docker
            systemctl enable docker

            docker login -u "${var.DOCKER_HUB_USER}" -p "${var.DOCKER_HUB_TOKEN}"
            docker pull ${var.DOCKER_HUB_USER}/backend:latest

            docker run -d --name backend \
              -p 8080:8080 \
              -e SECRET="${var.jwt_secret}" \
              -e ALLOWED_CONSUMER="*" \
              -e SPRING_DATASOURCE_URL="jdbc:mysql://${aws_db_instance.db.address}:3306/stockapp?createDatabaseIfNotExist=true" \
              -e SPRING_DATASOURCE_USERNAME="${var.db_username}" \
              -e SPRING_DATASOURCE_PASSWORD="${var.db_password}" \
              ${var.DOCKER_HUB_USER}/backend:latest
            EOF
}

resource "aws_security_group" "BACKEND" {
  vpc_id = aws_vpc.vpc.id

  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    security_groups = [aws_security_group.frontend.id]
  }
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # ⚠️ only for testing
  }

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

    #database----------------------
resource "aws_db_subnet_group" "db_subnet" {
  subnet_ids = [aws_subnet.private_subnet.id,aws_subnet.private_subnet2.id]
  tags = {Name="db subnet group name"}
}

resource "aws_db_instance" "db" {
  db_subnet_group_name = aws_db_subnet_group.db_subnet.name
  vpc_security_group_ids = [aws_security_group.db.id]

  identifier = "stockapp-db"
  allocated_storage = 10
  engine = "mysql"
  engine_version = "8.0"
  instance_class = "db.t3.micro"
  username = var.db_username
  password = var.db_password
  skip_final_snapshot = true
  availability_zone = "af-south-1a"
  multi_az = false

tags = {Name="stock app db"}
}

resource "aws_security_group" "db" {
  vpc_id = aws_vpc.vpc.id

  ingress {
    from_port = 3306
    to_port = 3306
    protocol = "tcp"
    security_groups = [aws_security_group.BACKEND.id]
  }

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
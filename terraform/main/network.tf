resource "aws_vpc" "vpc" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name= "stock_app_vpc"
  }
}

resource "aws_subnet" "private_subnet" {
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "af-south-1a"
  map_public_ip_on_launch = false
  tags = {
    Name= "private subnet"
  }
}
resource "aws_subnet" "private_subnet2" {
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.2.0/24"
  availability_zone = "af-south-1b"
  map_public_ip_on_launch = false
  tags = {
    Name= "private subnet 2"
  }
}

resource "aws_subnet" "public_subnet" {
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.3.0/24"
  map_public_ip_on_launch = true
  availability_zone = "af-south-1a"

  tags = {
    Name= "public subnet"
  }
}
resource "aws_subnet" "public_subnet2" {
  vpc_id = aws_vpc.vpc.id
  cidr_block = "10.0.4.0/24"
  map_public_ip_on_launch = true
  availability_zone = "af-south-1b"

  tags = {
    Name= "public subnet"
  }
}


resource "aws_internet_gateway" "IGW" {
  vpc_id = aws_vpc.vpc.id
  tags = {
    Name= "stock_app_internet gateway"
  }
}


resource "aws_route_table" "public_RT" {
  vpc_id = aws_vpc.vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.IGW.id
  }
}

resource "aws_route_table_association" "public_subnet" {
  route_table_id = aws_route_table.public_RT.id
  subnet_id = aws_subnet.public_subnet.id
}

resource "aws_eip" "eip"{
 domain = "vpc"
  tags = {
    Name="stackapp elastic ip"
  }
}

resource "aws_nat_gateway" "nat_GW" {
  subnet_id = aws_subnet.public_subnet.id
  allocation_id = aws_eip.eip.id
}


resource "aws_route_table" "privateRt" {
  vpc_id = aws_vpc.vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_GW.id
  }
  tags = {
    Name="private route table"
  }
}

resource "aws_route_table_association" "private" {
  route_table_id = aws_route_table.privateRt.id
  subnet_id = aws_subnet.private_subnet.id
}

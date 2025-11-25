# ###########################################
# # Security Group for Load Balancer
# ###########################################
# resource "aws_security_group" "alb_sg" {
#   name        = "alb-sg"
#   description = "Allow HTTP access to the ALB"
#   vpc_id      = aws_vpc.vpc.id
#
#   ingress {
#     from_port   = 80
#     to_port     = 80
#     protocol    = "tcp"
#     cidr_blocks = ["0.0.0.0/0"]
#   }
#
#   egress {
#     from_port   = 0
#     to_port     = 0
#     protocol    = "-1"
#     cidr_blocks = ["0.0.0.0/0"]
#   }
# }
#
# ###########################################
# # Application Load Balancer
# ###########################################
# resource "aws_lb" "backend_lb" {
#   name               = "backend-alb"
#   internal           = false
#   load_balancer_type = "application"
#   security_groups    = [aws_security_group.alb_sg.id]
#   subnets            = [aws_subnet.public_subnet.id,aws_subnet.public_subnet2.id]
#
#   enable_deletion_protection = false
# }
#
# ###########################################
# # Target Group for Backend
# ###########################################
# resource "aws_lb_target_group" "backend_tg" {
#   name     = "backend-tg"
#   port     = 8080
#   protocol = "HTTP"
#   vpc_id   = aws_vpc.vpc.id
#
#   health_check {
#     path                = "/"
#     port                = "8080"
#
#     matcher             = "200-399"
#   }
# }
#
# ###########################################
# # Register Backend Instance with Target Group
# ###########################################
# resource "aws_lb_target_group_attachment" "backend_attach" {
#   target_group_arn = aws_lb_target_group.backend_tg.arn
#   target_id        = aws_instance.backend.id
#   port             = 8080
# }
#
# ###########################################
# # Listener for HTTP Traffic
# ###########################################
# resource "aws_lb_listener" "backend_listener" {
#   load_balancer_arn = aws_lb.backend_lb.arn
#   port              = 80
#   protocol          = "HTTP"
#
#   default_action {
#     type             = "forward"
#     target_group_arn = aws_lb_target_group.backend_tg.arn
#   }
# }

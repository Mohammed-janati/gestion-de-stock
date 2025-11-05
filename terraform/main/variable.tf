variable "region" {
  default = "af-south-1"
  type= string
  description = "region of the cloud"

}
variable "frontAMI" {
  default = "ami-00578e5c7b5d64f2a"
  type= string
  description = "front end instance ami"

}
variable "frontinstanceType" {
  default = "t3.micro"
  type= string
  description = "front end instance type"

}
variable "backAMI" {
  default = "ami-00578e5c7b5d64f2a"
  type= string
  description = "back end instance ami"

}
variable "availableZone" {
  default = "af-south-1"
  type= string


}
variable "backinstanceType" {
  default = "t3.micro"
  type= string
  description = "back end instance type"

}
variable "db_username" {
  default = "admin"
  type= string
  description = "database username"

}
variable "db_password" {
  default = "MySQL_2025#db"
  type= string
  description = "database password"
  sensitive = true
}

variable "DOCKER_HUB_USER" {
  description = "Docker Hub username"
}

variable "DOCKER_HUB_TOKEN" {
  description = "Docker Hub access token"
  sensitive   = true
}

variable "jwt_secret" {
  description = "JWT secret key"
  sensitive   = true
}


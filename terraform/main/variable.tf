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
  default = "admin@123@"
  type= string
  description = "database password"
  sensitive = true
}


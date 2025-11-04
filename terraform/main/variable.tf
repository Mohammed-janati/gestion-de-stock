variable "region" {
  default = "af-south-1"
  type= string
  description = "region of the cloud"

}
variable "frontAMI" {
  default = ""
  type= string
  description = "front end instance ami"

}
variable "frontinstanceType" {
  default = ""
  type= string
  description = "front end instance type"

}
variable "backAMI" {
  default = ""
  type= string
  description = "back end instance ami"

}
variable "backinstanceType" {
  default = ""
  type= string
  description = "back end instance type"

}
variable "db_username" {
  default = ""
  type= string
  description = "database username"

}
variable "db_password" {
  default = ""
  type= string
  description = "database password"
  sensitive = true
}


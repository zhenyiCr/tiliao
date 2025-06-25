package com.example.entity;

public class Booking {
        private Integer id;
        private String picture;
        private Integer departmentId;
        private Integer money;
        private Integer doctorId;
        private String departmentName;
        private String doctorName;
        private String doctorAvatar;

        public String getDoctorAvatar() {return doctorAvatar;}

        public void setDoctorAvatar(String doctorAvatar) {this.doctorAvatar = doctorAvatar;}

        public String getDepartmentName() {return departmentName;}

        public void setDepartmentName(String departmentName) {this.departmentName = departmentName;}

        public String getDoctorName() {return doctorName;}

        public void setDoctorName(String doctorName) {this.doctorName = doctorName;}

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {this.id = id;}

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public Integer getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Integer departmentId) {
            this.departmentId = departmentId;
        }

        public Integer getMoney() {
            return money;
        }

        public void setMoney(Integer money) {
            this.money = money;
        }

        public Integer getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(Integer doctorId) {
            this.doctorId = doctorId;
        }

}

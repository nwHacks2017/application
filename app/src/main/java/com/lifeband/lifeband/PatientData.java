package com.lifeband.lifeband;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientData implements Parcelable{
    private String id;
    private Information information;
    private EmergencyContact emergencyContact;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public static PatientData fromJsonObject(JSONObject object) throws JSONException {

        PatientData p = new PatientData();
        if (object.has("id")) {
            p.setId(object.getString("id"));
        }
        if (object.has("information")) {
            p.setInformation(new PatientData.Information());
            JSONObject informationJson = object.getJSONObject("information");
            if (informationJson.has("fullName")) {
                p.getInformation().setFullName(informationJson.getString("fullName"));
            }
            if (informationJson.has("gender")) {
                p.getInformation().setGender(informationJson.getString("gender"));
            }
            if (informationJson.has("address")) {
                p.getInformation().setAddress(informationJson.getString("address"));
            }
            if (informationJson.has("birth")) {
                p.getInformation().setBirth(new PatientData.Information.Date());
                JSONObject birthJson = informationJson.getJSONObject("birth");
                if (birthJson.has("day")) {
                    p.getInformation().getBirth().setDay(birthJson.getString("day"));
                }
                if (birthJson.has("month")) {
                    p.getInformation().getBirth().setMonth(birthJson.getString("month"));
                }
                if (birthJson.has("year")) {
                    p.getInformation().getBirth().setYear(birthJson.getString("year"));
                }
            }
        }

        return p;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getId());
    }

    public static class Information  implements Parcelable {
        private String fullName;
        private String gender;
        private String address;
        private Date birth;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(getFullName());
            dest.writeString(getGender());
            dest.writeString(getAddress());
        }

        public static class Date implements Parcelable {
            private String day;
            private String month;
            private String year;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(getDay());
                dest.writeString(getMonth());
                dest.writeString(getYear());
            }
        }
    }

    public static class EmergencyContact implements Parcelable {
        private String fullName;
        private String phone;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(getFullName());
            dest.writeString(getPhone());
        }
    }
}
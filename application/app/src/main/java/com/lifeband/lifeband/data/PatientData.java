package com.lifeband.lifeband.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientData implements Parcelable{

    private String id;
    private Information information;
    private EmergencyContact emergencyContact;

    public PatientData() {
    }

    public PatientData(Parcel p) {
        id = p.readString();
        information = p.readParcelable(Information.class.getClassLoader());
        emergencyContact = p.readParcelable(EmergencyContact.class.getClassLoader());
    }

    public static final Creator<PatientData> CREATOR = new Creator<PatientData>() {
        @Override
        public PatientData createFromParcel(Parcel p) {
            return new PatientData(p);
        }

        @Override
        public PatientData[] newArray(int size) {
            return new PatientData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {
        p.writeString(id);
        p.writeParcelable(information, flags);
        p.writeParcelable(emergencyContact, flags);
    }

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
            if (informationJson.has("fullname")) {
                p.getInformation().setFullName(informationJson.getString("fullname"));
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

    public static class Information  implements Parcelable {

        private String fullName;
        private String gender;
        private String address;
        private Date birth;

        public Information() {
        }

        public Information(Parcel p) {
            fullName = p.readString();
            gender = p.readString();
            address = p.readString();
            birth = p.readParcelable(Date.class.getClassLoader());
        }

        public static final Creator<Information> CREATOR = new Creator<Information>() {
            @Override
            public Information createFromParcel(Parcel p) {
                return new Information(p);
            }

            @Override
            public Information[] newArray(int size) {
                return new Information[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel p, int flags) {
            p.writeString(getFullName());
            p.writeString(getGender());
            p.writeString(getAddress());
            p.writeParcelable(birth, flags);
        }

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

        public static class Date implements Parcelable {

            private String day;
            private String month;
            private String year;

            public Date() {
            }

            public Date(Parcel p) {
                day = p.readString();
                month = p.readString();
                year = p.readString();
            }

            public static final Creator<Date> CREATOR = new Creator<Date>() {
                @Override
                public Date createFromParcel(Parcel p) {
                    return new Date(p);
                }

                @Override
                public Date[] newArray(int size) {
                    return new Date[size];
                }
            };

            @Override
            public void writeToParcel(Parcel p, int flags) {
                p.writeString(getDay());
                p.writeString(getMonth());
                p.writeString(getYear());
            }

            @Override
            public int describeContents() {
                return 0;
            }

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

        }
    }

    public static class EmergencyContact implements Parcelable {

        private String fullName;
        private String phone;

        public EmergencyContact() {
        }

        public EmergencyContact(Parcel p) {
            fullName = p.readString();
            phone = p.readString();
        }

        public static final Creator<EmergencyContact> CREATOR = new Creator<EmergencyContact>() {
            @Override
            public EmergencyContact createFromParcel(Parcel p) {
                return new EmergencyContact(p);
            }

            @Override
            public EmergencyContact[] newArray(int size) {
                return new EmergencyContact[size];
            }
        };

        @Override
        public void writeToParcel(Parcel p, int flags) {
            p.writeString(getFullName());
            p.writeString(getPhone());
        }

        @Override
        public int describeContents() {
            return 0;
        }

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
    }
}
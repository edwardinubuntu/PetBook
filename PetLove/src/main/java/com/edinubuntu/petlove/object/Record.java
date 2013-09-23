package com.edinubuntu.petlove.object;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by edward_chiang on 13/8/12.
 */
@Table(name = "Records")
public class Record extends Model {

    @Column(name = "AcceptNumber")
    private String acceptNumber;

    @Column(name = "RecordId")
    private int recordId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Sex")
    private String sex;

    @Column(name = "Type")
    private String type;

    @Column(name = "Build")
    private String build;

    @Column(name = "Age")
    private String age;

    @Column(name = "Variety")
    private String variety;

    @Column(name = "Reason")
    private String reason;

    @Column(name = "ChipNumber")
    private String chipNumber;

    @Column(name = "IsSterilization")
    private String isSterilization;

    @Column(name = "HairType")
    private String hairType;

    @Column(name = "Note")
    private String note;

    @Column(name = "Resettlement")
    private String resettlement;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Email")
    private String email;

    @Column(name = "ChildreAnlog")
    private String childreAnlong;

    @Column(name = "AnimalAnlong")
    private String animalAnlong;

    @Column(name = "bodyWeight")
    private String bodyWeight;

    @Column(name = "ImageName")
    private String imageName;

    public String getAcceptNumber() {
        return acceptNumber;
    }

    public void setAcceptNumber(String acceptNumber) {
        this.acceptNumber = acceptNumber;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }

    public String getSterilization() {
        return isSterilization;
    }

    public void setSterilization(String sterilization) {
        isSterilization = sterilization;
    }

    public String getHairType() {
        return hairType;
    }

    public void setHairType(String hairType) {
        this.hairType = hairType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getResettlement() {
        return resettlement;
    }

    public void setResettlement(String resettlement) {
        this.resettlement = resettlement;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChildreAnlong() {
        return childreAnlong;
    }

    public void setChildreAnlong(String childreAnlong) {
        this.childreAnlong = childreAnlong;
    }

    public String getAnimalAnlong() {
        return animalAnlong;
    }

    public void setAnimalAnlong(String animalAnlong) {
        this.animalAnlong = animalAnlong;
    }

    public String getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(String bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;

        Record record = (Record) o;

        if (acceptNumber != null ? !acceptNumber.equals(record.acceptNumber) : record.acceptNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return acceptNumber != null ? acceptNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Record{" +
                "acceptNumber='" + acceptNumber + '\'' +
                ", recordId=" + recordId +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", type='" + type + '\'' +
                ", build='" + build + '\'' +
                ", age='" + age + '\'' +
                ", variety='" + variety + '\'' +
                ", reason='" + reason + '\'' +
                ", chipNumber='" + chipNumber + '\'' +
                ", isSterilization='" + isSterilization + '\'' +
                ", hairType='" + hairType + '\'' +
                ", note='" + note + '\'' +
                ", resettlement='" + resettlement + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", childreAnlong='" + childreAnlong + '\'' +
                ", animalAnlong='" + animalAnlong + '\'' +
                ", bodyWeight='" + bodyWeight + '\'' +
                ", imageName='" + imageName + '\'' +
                "} " + super.toString();
    }
}

package recommenderSystem.domain;

import javax.persistence.*;

@Table(name = "news")
@Entity
public class New {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "news_id")
    Integer newId;
    @Column(name = "news_title")
    String newTitle;
    @Column(name = "news_content")
    String newContent;
    @Column(name = "type_id")
    Integer typeId;
    @Column(name = "news_creattime")
    String newCreattime;
    @Column(name = "news_recourse")
    String newRecourse;
    @Column(name = "news_link")
    String newLink;
    @Column(name = "heat_num")
    Integer heatNum;

    public Integer getHeatNum() {
        return heatNum;
    }

    public void setHeatNum(Integer heatNum) {
        this.heatNum = heatNum;
    }

    public Integer getNewId() {
        return newId;
    }

    public void setNewId(Integer newId) {
        this.newId = newId;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getNewCreattime() {
        return newCreattime;
    }

    public void setNewCreattime(String newCreattime) {
        this.newCreattime = newCreattime;
    }

    public String getNewRecourse() {
        return newRecourse;
    }

    public void setNewRecourse(String newRecourse) {
        this.newRecourse = newRecourse;
    }

    public String getNewLink() {
        return newLink;
    }

    public void setNewLink(String newLink) {
        this.newLink = newLink;
    }
}

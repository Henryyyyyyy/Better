package me.henry.versatile.model.note;

import java.io.Serializable;
import java.util.ArrayList;

import me.henry.versatile.db.annotation.Column;
import me.henry.versatile.db.annotation.Table;

/**
 * Created by henry on 2018/1/5.
 */
@Table(name = "note")
public class Note implements Serializable{
    @Column(id = true, auto_increment = true)
    private long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(type = Column.ColumnType.TMANY, autofresh = true)
    private ArrayList<Punch> punches;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<Punch> getPunches() {
        return punches;
    }

    public void setPunches(ArrayList<Punch> punches) {
        this.punches = punches;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", punches=" + punches +
                '}';
    }
}

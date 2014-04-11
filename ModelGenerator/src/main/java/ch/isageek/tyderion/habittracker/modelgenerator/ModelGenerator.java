package ch.isageek.tyderion.habittracker.modelgenerator;

import de.greenrobot.daogenerator.Annotation;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class ModelGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "ch.isageek.tyderion.habittracker.model");
        schema.enableKeepSectionsByDefault();


        Entity habit = schema.addEntity("Habit");
        habit.addIdProperty();
        habit.addDateProperty("dateCreated");
        habit.addStringProperty("name").unique();
        habit.addBooleanProperty("isPositive");
        habit.addStringProperty("description");

        Entity occurence = schema.addEntity("Occurence");
        occurence.addIdProperty();
        occurence.addDateProperty("date");
        Property habitID = occurence.addLongProperty("habitID").getProperty();
        occurence.addToOne(habit, habitID);
        habit.addToMany(occurence, habitID);
//        occurence.addToMany(habit, habitID);

        new DaoGenerator().generateAll(schema, "src-gen/main/java");
    }
}
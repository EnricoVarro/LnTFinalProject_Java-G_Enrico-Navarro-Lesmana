module FinalProjectEnricoNavarroLesmana {
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	requires java.sql;
	
	opens main;
	opens model;
	opens view;
	opens database;
}
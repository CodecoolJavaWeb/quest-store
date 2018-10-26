package com.codecool.quest.store.controller.dao;


import java.sql.Connection;

public class DAOFactory {

    private Connection connection;
    private ArtifactDAO artifactDAO;
    private ClassDAO classDAO;
    private CodecoolerDAO codecoolerDAO;
    private LevelDAO levelDAO;
    private LoginDAO loginDAO;
    private MentorDAO mentorDAO;
    private QuestDAO questDAO;
    private SessionDAO sessionDAO;
    private TeamDAO teamDAO;

    public DAOFactory(Connection connection) {
        this.connection = connection;
        this.artifactDAO = new DbArtifactDAO(this.connection);
        this.classDAO = new DbClassDAO(this.connection);
        this.codecoolerDAO = new DbCodecoolerDAO(this.connection);
        this.levelDAO = new DbLevelDAO(this.connection);
        this.loginDAO = new DbLoginDAO(this.connection);
        this.mentorDAO = new DbMentorDAO(this.connection);
        this.questDAO = new DbQuestDAO(this.connection);
        this.sessionDAO = new DbSessionDAO(this.connection);
        this.teamDAO = new DbTeamDAO(this.connection);

    }


    public Connection getConnection() {
        return connection;
    }

    public ArtifactDAO getArtifactDAO() {
        return artifactDAO;
    }

    public ClassDAO getClassDAO() {
        return classDAO;
    }

    public CodecoolerDAO getCodecoolerDAO() {
        return codecoolerDAO;
    }

    public LevelDAO getLevelDAO() {
        return levelDAO;
    }

    public LoginDAO getLoginDAO() {
        return loginDAO;
    }

    public MentorDAO getMentorDAO() {
        return mentorDAO;
    }

    public QuestDAO getQuestDAO() {
        return questDAO;
    }

    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

    public TeamDAO getTeamDAO() {
        return teamDAO;
    }




}

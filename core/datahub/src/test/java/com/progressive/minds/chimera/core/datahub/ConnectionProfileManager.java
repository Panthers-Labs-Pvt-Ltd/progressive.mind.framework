package com.progressive.minds.chimera.core.datahub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ConnectionProfileManager extends JFrame {
    private JTextField projectIdField;
    private JTextField gitlabTokenField;
    private JTextField profileNameField;
    private JTextField hostField;
    private JTextField userField;

    private DefaultListModel<String> profileListModel;
    private JList<String> profileList;

    private ArrayList<ConnectionProfile> profiles;

    public ConnectionProfileManager() {
        setTitle("Connection Profile Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        profiles = new ArrayList<>();

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2));

        formPanel.add(new JLabel("Profile Name:"));
        profileNameField = new JTextField();
        formPanel.add(profileNameField);

        formPanel.add(new JLabel("Project ID:"));
        projectIdField = new JTextField();
        formPanel.add(projectIdField);

        formPanel.add(new JLabel("GitLab Token:"));
        gitlabTokenField = new JTextField();
        formPanel.add(gitlabTokenField);

        formPanel.add(new JLabel("Host:"));
        hostField = new JTextField();
        formPanel.add(hostField);

        formPanel.add(new JLabel("User:"));
        userField = new JTextField();
        formPanel.add(userField);

        // Profile List Panel
        profileListModel = new DefaultListModel<>();
        profileList = new JList<>(profileListModel);
        JScrollPane listScrollPane = new JScrollPane(profileList);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton testConnectionButton = new JButton("Test Connection");
        JButton loadButton = new JButton("Load Existing");

        buttonPanel.add(saveButton);
        buttonPanel.add(testConnectionButton);
        buttonPanel.add(loadButton);

        // Action Listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });

        testConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testConnection();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProfile();
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void saveProfile() {
        String profileName = profileNameField.getText();
        String projectId = projectIdField.getText();
        String gitlabToken = gitlabTokenField.getText();
        String host = hostField.getText();
        String user = userField.getText();

        if (profileName.isEmpty() || projectId.isEmpty() || gitlabToken.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        ConnectionProfile profile = new ConnectionProfile(profileName, projectId, gitlabToken, host, user);
        profiles.add(profile);
        profileListModel.addElement(profileName);
        JOptionPane.showMessageDialog(this, "Profile saved successfully!");
    }

    private void testConnection() {
        String projectId = projectIdField.getText();
        String gitlabToken = gitlabTokenField.getText();
        if (projectId.isEmpty() || gitlabToken.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Project ID and GitLab Token are required to test connection.");
            return;
        }
        JOptionPane.showMessageDialog(this, "Connection successful! (Simulation)");
    }

    private void loadProfile() {
        String selectedProfile = profileList.getSelectedValue();
        if (selectedProfile == null) {
            JOptionPane.showMessageDialog(this, "Please select a profile to load.");
            return;
        }

        for (ConnectionProfile profile : profiles) {
            if (profile.getProfileName().equals(selectedProfile)) {
                profileNameField.setText(profile.getProfileName());
                projectIdField.setText(profile.getProjectId());
                gitlabTokenField.setText(profile.getGitlabToken());
                hostField.setText(profile.getHost());
                userField.setText(profile.getUser());
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConnectionProfileManager::new);
    }
}

class ConnectionProfile {
    private String profileName;
    private String projectId;
    private String gitlabToken;
    private String host;
    private String user;

    public ConnectionProfile(String profileName, String projectId, String gitlabToken, String host, String user) {
        this.profileName = profileName;
        this.projectId = projectId;
        this.gitlabToken = gitlabToken;
        this.host = host;
        this.user = user;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getGitlabToken() {
        return gitlabToken;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }
}

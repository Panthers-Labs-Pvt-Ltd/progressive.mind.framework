package com.progressive.minds.keyclock;

import com.progressive.minds.keyclock.config.KeycloakSetupProperties;
import com.progressive.minds.keyclock.service.*;
import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class KeycloakInitializerRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(KeycloakInitializerRunner.class);

    private final Keycloak keycloak;
    private final KeycloakSetupProperties setupProperties;
    private final RealmSetupService realmSetupService;
    private final ClientSetupService clientSetupService;
    private final RoleSetupService roleSetupService;
    private final GroupSetupService groupSetupService;
    private final UserSetupService userSetupService;

    public KeycloakInitializerRunner(Keycloak keycloak,
                                     KeycloakSetupProperties setupProperties,
                                     RealmSetupService realmSetupService,
                                     ClientSetupService clientSetupService,
                                     RoleSetupService roleSetupService,
                                     GroupSetupService groupSetupService,
                                     UserSetupService userSetupService) {
        this.keycloak = keycloak;
        this.setupProperties = setupProperties;
        this.realmSetupService = realmSetupService;
        this.clientSetupService = clientSetupService;
        this.roleSetupService = roleSetupService;
        this.groupSetupService = groupSetupService;
        this.userSetupService = userSetupService;
    }

    @Override
    public void run(String... args) {
        log.info("Starting Keycloak initialization...");

        String realmName = setupProperties.getRealm();

        realmSetupService.createRealm(realmName);
        clientSetupService.createClient(realmName, setupProperties.getClient());
        roleSetupService.createRoles(realmName, setupProperties.getRoles());
        groupSetupService.createGroups(realmName, setupProperties.getGroups());
        userSetupService.createUsers(realmName, setupProperties.getUsers());

        log.info("Keycloak initialization completed successfully");
    }
}

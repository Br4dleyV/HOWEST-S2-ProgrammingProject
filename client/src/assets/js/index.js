import * as LocalStorageAbstractor from "./data-connector/local-storage-abstractor.js";
import { setupNavigation } from "./navigator.js";
import { setupExitLobbyPopup } from "./exit-lobby-popup.js";
import { setupSwitchProfilePage } from "./switch-profile.js";
import { setupProfilePage } from "./profile.js";
import { hostGameFunctions } from "./host-game.js";
import { refreshGames } from "./join-game.js";
import * as Helper from "./util/helpers.js";

function init() {
    checkUsernameAndAvatar();
    setupNavigation();
    executePageSpecificFunction();
}

function executePageSpecificFunction() {
    const pageSetupFunctions = {
        "host-game-page": setupHostGamePage, hostGameFunctions,
        "join-game-page": setupJoinGamePage,
        "lobby-page": setupExitLobbyPopup,
        "profile-page": setupProfilePage,
        "switch-profile-page": setupSwitchProfilePage,
        "index-page": setupIndexPage,
        "exit-lobby-page": setupExitLobbyPopup,
    };

    const pageId = document.body.id;
    if (pageSetupFunctions[pageId]) {
        pageSetupFunctions[pageId]();
    } else {
        console.warn(`No functions defined for page ID: ${pageId}`);
    }
}



function setupHostGamePage() {
    const playerInfoElement = document.querySelector("#player-info");

    if (!playerInfoElement) {
        console.error("Error: #player-info element not found in the DOM.");
        return;
    }

    Helper.renderProfiles(document.querySelector("#player-info"));
    hostGameFunctions();
}

function setupJoinGamePage() {
    const playerInfoElement = document.querySelector("#player-info");

    if (!playerInfoElement) {
        console.error("Error: #player-info element not found in the DOM.");
        return;
    }
    
    Helper.renderProfiles(document.querySelector("#player-info"));
    refreshGames();
}

function setupIndexPage() {
    const playerInfoElement = document.querySelector("#player-info");

    if (!playerInfoElement) {
        console.error("Error: #player-info element not found in the DOM.");
        return;
    }

    Helper.renderProfiles(playerInfoElement);
}

function checkUsernameAndAvatar() {
    if (!LocalStorageAbstractor.loadFromStorage('playerName')) {
        LocalStorageAbstractor.saveToStorage('playerName', 'Player');
    }
    if (!LocalStorageAbstractor.loadFromStorage("avatar")) {
        LocalStorageAbstractor.saveToStorage("avatar", "assets/images/profiles/profile_anonymous.jpg");
    }
}

init();
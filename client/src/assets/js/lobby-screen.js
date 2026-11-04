import * as LocalStorageAbstractor from "./data-connector/local-storage-abstractor.js";
import * as API from './api.js';
import * as Helper from './util/helpers.js';

function init() {
    const gameId = LocalStorageAbstractor.loadFromStorage('gameId');
    API.getGameInfo(gameId).then(gameInfo => {
        changeGameLayout(gameInfo.game, gameId);
        renderPlayers(gameInfo.game);
    }).then(() => {
        lobbyLoop(gameId);
    });
}

function lobbyLoop(gameId) {
    const knownPlayerCount = document.querySelector("#player-count").dataset.count;
    const time = 2000;

    API.getGameInfo(gameId)
        .then(gameInfo => {
            if(gameInfo.game.players.length > knownPlayerCount) {
                changeGameLayout(gameInfo.game, gameId);
                renderPlayers(gameInfo.game);
            }
            if(gameInfo.game.players.length === gameInfo.game.numberOfPlayers){
                startGame();
            }else{
                setTimeout(() => lobbyLoop(gameId), time);
            }
        })
        .catch((err) => {
            console.error("something went wrong: " + err);
        });
}

function changeGameLayout(gameInfo, gameId) {
    document.querySelector("#Lobby-screen h1").textContent = gameInfo.gameName;
    document.querySelector("#lobby-page nav span").textContent = gameId;
    document.querySelector("#player-count").textContent = `${gameInfo.players.length}/${gameInfo.numberOfPlayers}`;
    document.querySelector("#player-count").dataset.count = gameInfo.players.length;
}

function renderPlayers(gameInfo){
    const $container = document.querySelector("#Lobby-screen .gridcontainer");
    $container.innerHTML = '';
    let playerCount = 0; // Currently uses to see if owner, will be deleted when able to use server
    gameInfo.players.forEach(player => {
        playerCount ++;
        let playerName = player;
        if(typeof player !== "string"){
            playerName = player.name;
        }
        $container.insertAdjacentHTML("beforeend",renderPlayer(playerName, playerCount));
    });
}

function renderPlayer(playerName,playerCount){
    const $template = document.querySelector("#lobby-screen-player-template").content.firstElementChild.cloneNode(true);

    $template.querySelector("img").src = getAvatar(playerName);
    $template.querySelector("h2").textContent = getName(playerCount, playerName);

    return $template.outerHTML;
}

function getName(playerCount, playerName) {
    let name = Helper.playerIsCurrent(playerName) ? "You" : playerName;
    name += playerIsOwner(playerCount) ? " (Owner)" : "";
    return name;
}

function getAvatar(playerName) {
    if(Helper.playerIsCurrent(playerName)){
        return LocalStorageAbstractor.loadFromStorage("avatar");
    }else{
        return "./assets/images/profile-img-template.gif";
    }
}

function playerIsOwner(playerCount){
    // check if owner
    // Just using a count where the first player is owner, later using server.
    return playerCount === 1;
}

function startGame() {
    window.location.href = "game-screen.html";
}

init();
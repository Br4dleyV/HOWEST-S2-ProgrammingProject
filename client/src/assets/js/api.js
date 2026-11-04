import * as CommunicationAbstractor from "./data-connector/api-communication-abstractor.js";
import * as LocalStorageAbstractor from "./data-connector/local-storage-abstractor.js";

function getGameInfo(gameId) {
    return CommunicationAbstractor.fetchFromServer(`/games/${gameId}`, "GET");
}

function getGames() {
    return CommunicationAbstractor.fetchFromServer("/games", "GET").then((res) => {
        return res.games;
    });
}

function joinGame(gameId) {
    const playerName = LocalStorageAbstractor.loadFromStorage('playerName');
    return CommunicationAbstractor.fetchFromServer(`/games/${gameId}/players/${playerName}`, "POST").then((res) => {
        return res;
    });
}

function createGameOnServer(strGameName, playerCount) {
    return CommunicationAbstractor.fetchFromServer('/games', 'POST', {
        "gameName": strGameName,
        "numberOfPlayers": playerCount,
        "playerName": LocalStorageAbstractor.loadFromStorage('playerName'), 
        "profileImageName": LocalStorageAbstractor.loadFromStorage('avatar'),
    });
}

function getGems() {
    return CommunicationAbstractor.fetchFromServer("/gems", "GET").then((res) => {
        return res.gems;
    });
}

function getNobles() {
    return CommunicationAbstractor.fetchFromServer("/nobles", "GET").then((res) => {
        return res.nobles;
    });
}

function getDevelopments() {
    return CommunicationAbstractor.fetchFromServer("/developments", "GET").then((res) => {
        return res.developments;
    });
}

function takeTokens(tokens) {
    const playerName = LocalStorageAbstractor.loadFromStorage('playerName');
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");

    return CommunicationAbstractor.fetchFromServer(`/games/${gameId}/players/${playerName}/tokens`, "PATCH", {
        "take": tokens
    });
}

function buyDevelopment(name, price) {
    const playerName = LocalStorageAbstractor.loadFromStorage('playerName');
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");

    return CommunicationAbstractor.fetchFromServer(`/games/${gameId}/players/${playerName}/developments`, "POST", {
        "development": {
            name : name
        },
        "payment": price
    });
}

function takeNoble(nobleName) {
    const playerName = LocalStorageAbstractor.loadFromStorage('playerName');
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");

    return CommunicationAbstractor.fetchFromServer(`/games/${gameId}/players/${playerName}/nobles`, "POST", {
        name: nobleName
    });
}

function reserveDevelopment(cardName) {
    const playerName = LocalStorageAbstractor.loadFromStorage('playerName');
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");

    return CommunicationAbstractor.fetchFromServer(
        `/games/${gameId}/players/${playerName}/reserve`, "POST",
        {
            development: {
                name : cardName
            }
        }
    );
}

function buyReserveDevelopment(cardName, price) {
    const playerName = LocalStorageAbstractor.loadFromStorage('playerName');
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");

    return CommunicationAbstractor.fetchFromServer(
        `/games/${gameId}/players/${playerName}/reserve/${cardName}`, "DELETE",
        {
            payment: price
        }
    );
}

export { getGameInfo, getGames, joinGame, createGameOnServer, getGems, getNobles, getDevelopments, takeTokens , buyDevelopment, reserveDevelopment, buyReserveDevelopment, takeNoble };
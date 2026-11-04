import * as LocalStorageAbstractor from "../data-connector/local-storage-abstractor.js";
import * as API from "../api.js";

function renderProfiles(playerInfoElement) {
    if (!playerInfoElement) {
        console.error("Error: Player info element not found in the DOM.");
        return;
    }

    // Check for avatar and player name elements
    const avatarDisplay = playerInfoElement.querySelector("#player-display") || playerInfoElement.querySelector("#avatar-display");
    const playerNameElement = playerInfoElement.querySelector("#player-name");

    // Load username and avatar from localStorage
    const savedUsername = LocalStorageAbstractor.loadFromStorage("playerName");
    const savedAvatar = LocalStorageAbstractor.loadFromStorage("avatar");

    // Update the avatar if it exists
    if (avatarDisplay) {
        avatarDisplay.src = savedAvatar || "assets/images/profiles/profile_anonymous.jpg";;
    } else {
        console.warn("Avatar element not found. Skipping avatar update.");
    }

    // Update the player name if it exists
    if (playerNameElement) {
        playerNameElement.textContent = savedUsername || "Player Name";
    } else {
        console.warn("Player name element not found. Skipping player name update.");
    }
}

function playerIsCurrent(player){
    return player === LocalStorageAbstractor.loadFromStorage('playerName');
}

function getAdjustedCostWithGoldUsage(deductedCost, goldUsed = {}) {
    const adjustedCost = { ...deductedCost }; // copy's deducted cost
    let totalGoldUsed = 0;

    for (const token in goldUsed) {
        const goldAmount = goldUsed[token];
        if (adjustedCost[token] !== undefined) {
            adjustedCost[token] = Math.max(0, adjustedCost[token] - goldAmount);
        }
        totalGoldUsed += goldAmount;
    }

    if (totalGoldUsed > 0) {
        adjustedCost.GOLD = totalGoldUsed;
    }

    return adjustedCost;
}

function getDevelopmentDeductedPrice(cardName) {
    return developmentPriceByName(cardName).then((deductedCost) => {
        return getPlayerInfo().then((playerInfo) => {
            for (const token in deductedCost) {
                if (token in playerInfo.bonuses.tokens) {
                    deductedCost[token] -= playerInfo.bonuses.tokens[token];
                    if (deductedCost[token] < 0) deductedCost[token] = 0;
                }
            }
            return deductedCost;
        });
    });
}

function developmentPriceByName(cardName) {
    return API.getDevelopments()
        .then((developments) => {
            const development = developments.find(dev => dev.name === cardName);

            return development.cost.tokens;
        });
}

function getPlayerInfo(){
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");
    return API.getGameInfo(gameId).then((gameInfo) => {
        const players = gameInfo.game.players;
        for(const player of players) {
            if(playerIsCurrent(player.name)) {
                return player;
            }
        }
        return undefined;
    });
}

function hasGold(){
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");
    return API.getGameInfo(gameId)
        .then((gameInfo) => {
            const gold = gameInfo.game.currentPlayer.tokens.tokens.GOLD;
            return gold > 0;
        });
}

export { playerIsCurrent, renderProfiles,developmentPriceByName, getPlayerInfo, getDevelopmentDeductedPrice, getAdjustedCostWithGoldUsage,hasGold };
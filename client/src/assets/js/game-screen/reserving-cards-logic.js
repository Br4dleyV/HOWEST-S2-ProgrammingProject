import * as API from "../api.js";
import * as Helper from "../util/helpers.js";
import {buildFormTemplate}  from "./buying-card-logic.js";

import {renderFullBoard} from "./renderer.js";
import {gameLoop} from "../game-screen.js";
import {showModal} from "../util/modal.js";


function toggleReserveEventListeners(bool) {
    const cards = document.querySelectorAll("#right-panel .reserved-cards .card");

    cards.forEach((card) => {
        if (bool) {
            card.addEventListener("click", handleReservedCardClick);
        } else{
            card.removeEventListener("click", handleReservedCardClick);
        }
    });

}

function handleReservedCardClick(e) {
    const cardName = e.currentTarget.dataset.cardName;
    Helper.hasGold().then((hasGold) => {
        handlePurchase(cardName, hasGold);
    });
}

function handlePurchase(cardName, hasGold) {
    if (hasGold) {
        showGoldUsageModal(cardName);
    } else {
        showBuyConfirmationModal(cardName);
    }
}

function showGoldUsageModal(cardName) {
    buildFormTemplate(cardName).then(($formTemplate) => {
        showModal({
            title: "Do you want to use any gold?",
            body: $formTemplate,
            buttons: [
                { text: "Buy", value: "buy" },
                { text: "Cancel", value: "cancel" }
            ],
            onClose: (choice) => handleGoldModalClose(choice, cardName)
        });
    });
}

function handleGoldModalClose(choice, cardName) {
    if (choice === "buy") {
        const modalElement = document.querySelector('#modal-overlay');
        const goldInputs = modalElement.querySelectorAll(".gold-counters");
        const goldAmounts = {};
        goldInputs.forEach(input => {
            const token = input.name.toUpperCase();
            goldAmounts[token] = parseInt(input.value);
        });
        buyReserved(cardName, goldAmounts).catch(err => {
            console.error(err);
        });
    }
}

function showBuyConfirmationModal(cardName) {
    showModal({
        title: "Choose Action",
        body: `Do you want to buy ${cardName}?`,
        buttons: [
            { text: "Buy", value: "buy" },
            { text: "Cancel", value: "cancel" }
        ],
        onClose: (choice) => handleBuyConfirmationClose(choice, cardName)
    });
}

function handleBuyConfirmationClose(choice, cardName) {
    if (choice === "buy") {
        buyReserved(cardName).catch(err => {
            console.error(err);
        });
    }
}

function buyReserved(cardName, goldUsed = {}) {
    return Helper.getDevelopmentDeductedPrice(cardName).then((deductedCost) => {
        const adjustedCost = Helper.getAdjustedCostWithGoldUsage(deductedCost, goldUsed);
        return API.buyReserveDevelopment(cardName, adjustedCost);
    }).then(() => {
            showModal({
                title: "Success",
                body: "Successfully bought reserved card. Continuing to next player.",
                buttons: [{ text: "OK", value: "ok" }]
            });
    })
    .then(() => renderFullBoard())
    .then(() => gameLoop())
    .catch((err) => {
        showModal({
            title: "Cannot Buy",
            body: "You don't have enough tokens to buy this card.",
            buttons: [{ text: "OK", value: "ok" }]
        });
        console.error(err);
    });
}


export { toggleReserveEventListeners , buyReserved };
import * as API from "../api.js";
import { showModal } from "../util/modal.js";
import {renderFullBoard} from "./renderer.js";
import {gameLoop} from "../game-screen.js";

// Enables or disables event listeners for the noble selection
function toggleNobleEventListeners(boolean) {
    const nobles = document.querySelectorAll("#nobles .noble");
    nobles.forEach((noble) => {
        if (boolean) {
            noble.addEventListener("click", takeNoble);
        } else {
            noble.removeEventListener("click", takeNoble);
        }
    });
}

// Takes a noble
function takeNoble(e) {
    e.preventDefault();
    const selectedNoble = { name: e.currentTarget.dataset.nobleName }; // Get the noble name from the clicked element

    API.takeNoble(selectedNoble.name).then((response) => { // Try to take the noble
        if (!response) { // Check if the response is valid
            showModal({
                title: "Action Failed", body: "Could not take the noble.",
                buttons: [{ text: "OK", value: "ok" }]
            });
            return null;
        }
        showModal({ // Show success message and refresh the board
            title: "Success",
            body: `Successfully took ${selectedNoble.name}.`,
            buttons: [{ text: "OK", value: "ok" }]
        });
    })
    .then(() => renderFullBoard())
        .then(() => gameLoop())
        .catch((err) => {
            showModal({
                title: "Cannot Take Noble", body: err.cause, buttons: [{ text: "OK", value: "ok" }]
            });
        });
}

export { toggleNobleEventListeners };
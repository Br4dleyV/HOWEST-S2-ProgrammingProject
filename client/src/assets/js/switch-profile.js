import * as StorageAbstractor from "./data-connector/local-storage-abstractor.js";

function setupSwitchProfilePage() {
    const avatarElements = document.querySelectorAll(".avatar");

    avatarElements.forEach(avatar => {
        avatar.addEventListener("click", () => {
            const selectedAvatar = avatar.getAttribute("data-avatar");

            // Save the selected avatar to storage
            StorageAbstractor.saveToStorage("avatar", selectedAvatar);

            // Redirect back to profile.html
            window.location.href = "profile.html";
        });
    });
}

export { setupSwitchProfilePage };
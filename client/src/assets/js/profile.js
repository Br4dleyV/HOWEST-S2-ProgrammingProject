import * as StorageAbstractor from "./data-connector/local-storage-abstractor.js";
import { showModal } from "../js/util/modal.js";

function setupProfilePage() {
    const userNameInput = document.querySelector("#user-name");
    const avatarImg = document.querySelector("#avatar-img img"); // Select the <img> inside the avatar link
    const profileForm = document.querySelector("#profile-form");
    const avatarLink = document.querySelector("#avatar-img");

    if (!userNameInput || !avatarImg || !profileForm || !avatarLink) {
        console.error("Error: One or more required elements are missing in the DOM.");
        return;
    }

    loadProfileData(userNameInput, avatarImg);
    setupFormSubmission(profileForm, userNameInput, avatarImg);
    setupAvatarClick(avatarLink);
}

function loadProfileData(userNameInput, avatarImg) {
    // Load profile data from storage
    const savedUserName = StorageAbstractor.loadFromStorage("playerName");
    const savedAvatar = StorageAbstractor.loadFromStorage("avatar");

    // Populate the form with saved data
    userNameInput.value = savedUserName || "";
    avatarImg.src = savedAvatar || "assets/images/profiles/profile_anonymous.jpg";
}

function setupFormSubmission(profileForm, userNameInput, avatarImg) {
    // Save profile data when the form is submitted
    profileForm.addEventListener("submit", (event) => {
        event.preventDefault(); // Prevent form submission

        const userName = userNameInput.value.trim();
        const avatar = avatarImg.src;

        if (!userName) {
            return;
        }

        // Save username and avatar to storage
        StorageAbstractor.saveToStorage("playerName", userName);
        StorageAbstractor.saveToStorage("avatar", avatar);

        // Show success modal
        showModal({
            title: "Panos Boss",
            body: "Your worker ID has succesfully updated.",
            buttons: [{ text: "OK", value: "close" }]
        });
    });
}

function setupAvatarClick(avatarLink) {
    // Redirect to switch-profile.html when the avatar is clicked
    avatarLink.addEventListener("click", (event) => {
        event.preventDefault(); // Prevent navigation
        window.location.href = "switch-profile.html";
    });
}

export { setupProfilePage };
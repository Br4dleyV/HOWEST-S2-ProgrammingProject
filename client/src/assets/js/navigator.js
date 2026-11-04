function setupNavigation() {
    // Check if the current page is index.html
    if (document.body.id === "index-page") {
        return;
    }

    // Handle back button
    const backButton = document.querySelector("#back-button");
    if (backButton) {
        backButton.addEventListener("click", () => {
            if (document.body.id === "rules-page") {
                window.location.href = "index.html";
            } else {
                window.history.back(); // Go back to the previous page
            }
        });
    }

    // Handle home button
    const homeButton = document.querySelector("#home-button");
    if (homeButton) {
        homeButton.addEventListener("click", () => {
            window.location.href = "index.html"; // Navigate to the home page
        });
    }
}

export { setupNavigation };
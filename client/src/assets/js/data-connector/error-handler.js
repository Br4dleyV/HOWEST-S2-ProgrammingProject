function generateVisualAPIErrorInConsole(error){
    console.error('%c%s','background-color: red;color: white','! An error occurred while calling the API');
    console.table(error);
}

function handleError(error){
    generateVisualAPIErrorInConsole(error);
}

export { handleError };

function convertDateTime(requestDateTime) {
    let datetime = new Date(requestDateTime);
    return "Le " + datetime.getUTCDate() + "/" + datetime.getUTCMonth() + "/" + datetime.getUTCFullYear();
}

export {convertDateTime}
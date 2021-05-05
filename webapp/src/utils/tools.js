
function convertDateTimeToStringDate(requestDateTime) {
    let datetime = new Date(requestDateTime);
    return "Le " + datetime.getUTCDate() + "/" + datetime.getUTCMonth() + "/" + datetime.getUTCFullYear();
}

function convertDateTimeToStringTime(requestDateTime) {
    let datetime = new Date(requestDateTime);
    return datetime.getUTCHours() + ":" + datetime.getUTCMinutes() + ":" + datetime.getUTCSeconds();
}

function encodeFile(file) {
    return new Promise((resolve, reject) => {
        var fileReader = new FileReader();
        fileReader.onload = function (fileLoadedEvent) {
            let base64 = fileLoadedEvent.target.result;
            resolve(base64);
        }
        fileReader.readAsDataURL(file)
    });
}

async function encodeFiles(files) {
    const returnedFiles = [];
    if (files.length > 0) {
        for (let i = 0; i < files.length; i++) {
            let photo = await encodeFile(files[i])
            returnedFiles[i] = {
                photo: photo,
            }
        }
    }
    return returnedFiles;
}

export {convertDateTimeToStringDate, convertDateTimeToStringTime, encodeFiles, encodeFile}
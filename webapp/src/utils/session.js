import callAPI from "../utils/api.js";

var currentUser;

async function getCurrentUser(){
  let userData = getUserSessionData();
  if (!currentUser && userData){
  try {
    await callAPI(
      "/api/auths/user",
      "GET",
      userData.token,
      undefined
    ).then((response) => {
        currentUser =  response;
  });
  }
  catch (err) {
    console.error("session.js::getCurrentUser", err);
  }
}
return currentUser;
}

const STORE_NAME = "user";

const getUserSessionData = () => {
  let retrievedUser = localStorage.getItem(STORE_NAME);
  if (!retrievedUser) {
    retrievedUser = sessionStorage.getItem(STORE_NAME);
  }
  return JSON.parse(retrievedUser);
};

const setUserSessionData = (user) => {
  const storageValue = JSON.stringify(user);
  sessionStorage.setItem(STORE_NAME, storageValue);
};

const setUserStorageData = (user) => {
  const storageValue = JSON.stringify(user);
  localStorage.setItem(STORE_NAME, storageValue);
};

const removeSessionData = () => {
  localStorage.removeItem(STORE_NAME);
  sessionStorage.removeItem(STORE_NAME);
};

const resetCurrentUser = () => {
  currentUser = null;
}

export {
  getUserSessionData,
  setUserSessionData,
  setUserStorageData,
  removeSessionData,
  getCurrentUser,
  currentUser,
  resetCurrentUser
};

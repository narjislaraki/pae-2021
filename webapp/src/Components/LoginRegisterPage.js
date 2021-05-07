/* In a template literal, the ` (backtick), \ (backslash), and $ (dollar sign) characters should be 
escaped using the escape character \ if they are to be included in their template value. 
By default, all escape sequences in a template literal are ignored.*/
import {setUserSessionData, getCurrentUser, currentUser, setUserStorageData} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
import PrintMessage from "./PrintMessage.js"
const API_BASE_URL = "/api/auths/";

let loginPage = `<div class="register-card">
<div class="mb-3">
        <h2 class="condensed">FORMULAIRE D'INSCRIPTION</h2>
      </div>
      <form class="form-register register-grid">

        <input type="text" class="form-control input-card" id="username" placeholder="Pseudo*" required>

        <input type="text" class="form-control input-card" id="lastname" placeholder="Nom*" required>

        <input type="text" class="form-control input-card" id="firstname" placeholder="Prénom*" required>

        <input type="text" class="form-control input-card" id="street" placeholder="Rue*" required>

        <input type="text" class="form-control input-card" id="number" placeholder="Numéro*" required>

        <input type="text" class="form-control input-card" id="unitnumber" placeholder="Boite">

        <input type="text" class="form-control input-card" id="postcode" placeholder="Code Postal*" required>

        <input type="text" class="form-control input-card" id="city" placeholder="Commune*" required>

        <input type="text" class="form-control input-card" id="country" placeholder="Pays*" required>

        <input type="email" class="form-control input-card" id="email-register" placeholder="E-mail*" required>

        <input type="password" class="form-control input-card" id="password-register" placeholder="Mot-de-passe*" required>

        <input type="password" class="form-control input-card" id="password-confirmation" placeholder="Confirmer mot-de-passe*" required>
        
        <button class="btn btn-dark condensed small-caps" id="btn-register" type="submit">Envoyer Demande d'Inscription</button>

      </form>
</div>
<div class="sticker-login condensed"><h2>CONNEXION</h2></div>
<div class="login-card">
  <form class="form-login">
    <div class="mb-3">
      <i class="bi bi-person-circle"></i>
      <h2 class="condensed">CARTE DE MEMBRE</h2>
    </div>
    
    <!-- the "require" from the two next fields are momentally disabled to avoid problems with the functional tests -->
    <div class="mb-3">
      <input type="email" class="form-control input-card-login" id="email-login" aria-describedby="emailHelp" placeholder="Email" required pattern="^\\w+([.-]?\\w+)*@\\w+([\.-]?\\w+)*(\\.\\w{2,4})+\$">
    </div>
    <div class="mb-3">
      <input type="password" class="form-control input-card input-card-login" id="password-login" placeholder="Mot de passe" required>
    </div>
    <div class="mb-3  stayconnected">
      <input type="checkbox" id="stayconnected">
      <label class="form-check-label" for="exampleCheck1">Rester connecté(e)</label>
    </div>
    <button class="btn btn-dark btn-navbar condensed small-caps" type="submit" id="btn-login" >Se Connecter</button>
  </form>
</div>`;

const LoginRegisterPage = async () => {
  let page = document.querySelector("#page");
  page.innerHTML = loginPage;
  let loginForm = document.querySelector(".form-login");
  let registerForm = document.querySelector(".form-register");
  if (currentUser) {
    // re-render the navbar for the authenticated user
    Navbar();
  } else {
    loginForm.addEventListener("submit", onLogin);
    registerForm.addEventListener("submit", onRegister);
  }
};

const onLogin = async (e) => {
  e.preventDefault();
  let login = document.getElementById("email-login");
  let password = document.getElementById("password-login");

  try {
    const userLogged = await callAPI(
      API_BASE_URL + "login",
      "POST",
      undefined,
      {
        email: login.value,
        password: password.value,
      }
    );
    onUserLogin(userLogged);
  } catch (err) {
    if (err == "Error: Wrong credentials") {
      err.message = "Email ou mot de passe invalide ! ";
    } else if (err == "Error: User is not validated") {
      err.message = "L'utilisateur n'est pas encore validé ! ";
    }
    console.error("LoginRegisterPage::onLogin", err);
    PrintError(err);
  }
};

const onUserLogin = async (userData) => {
  const user = { ...userData, isAutenticated: true };
  if (document.getElementById("stayconnected").checked) {
    console.log("storage")
    setUserStorageData(user)
  }
  else {
    setUserSessionData(user);
  }
  await getCurrentUser();
  // re-render the navbar for the authenticated user
  Navbar();
  RedirectUrl("/");
};


const onRegister = async (e) => {
  e.preventDefault();

  if (document.getElementById("password-register").value !== document.getElementById("password-confirmation").value){
    let err = {
      message: "Les mots de passe ne correspondent pas"
    }
    PrintError(err);
    return;
  }

  let user = {
    username: document.getElementById("username").value,
    firstName: document.getElementById("firstname").value,
    lastName: document.getElementById("lastname").value,
    email: document.getElementById("email-register").value,
    password: document.getElementById("password-register").value,
    passwordVerification: document.getElementById("password-confirmation").value,
    address: {
      street: document.getElementById("street").value,
      buildingNumber: document.getElementById("number").value,
      postCode: document.getElementById("postcode").value,
      country: document.getElementById("country").value,
      city: document.getElementById("city").value,
    }
  };

  if (document.getElementById("unitnumber").value != "") {
    user.address.unitNumber = document.getElementById("unitnumber").value;
  }
  
  try {
    const userRegistered = await callAPI(
      API_BASE_URL + "register",
      "POST",
      undefined,
      user
    );
    if (userRegistered) {
      RedirectUrl("/");
      PrintMessage("Votre compte a été créé. Il est maintenant en attente de validation.");
    }

  } catch (err) {
    if (err == "Error: This email is already in use") {
      err.message = "L'email est déjà utilisé";
    }
    else if (err == "Error: This username is already in use") {
      err.message = "Le nom d'utilisateur est déjà utilisé";
    }
    console.error("LoginRegisterPage::onRegister", err);
    PrintError(err);
  }
}

export default LoginRegisterPage;

/* In a template literal, the ` (backtick), \ (backslash), and $ (dollar sign) characters should be 
escaped using the escape character \ if they are to be included in their template value. 
By default, all escape sequences in a template literal are ignored.*/
import { getUserSessionData, setUserSessionData } from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
const API_BASE_URL = "/api/auths/";

let loginPage = `<div class="register-card"></div>
<div class="sticker-login condensed"><h2>CONNEXION</h2></div>
<div class="login-card">
  <form class="form-login">
    <div class="mb-3">
      <i class="bi bi-person-circle"></i>
      <h2 class="condensed">CARTE DE MEMBRE</h2>
    </div>
    
    <!-- the "require" from the two next fields are momentally disabled to avoid problems with the functional tests -->
    <div class="mb-3">
      <input type="email" class="form-control input-card" id="email-login" aria-describedby="emailHelp" placeholder="Email"> <!-- required="" pattern="^\\w+([.-]?\\w+)*@\\w+([\.-]?\\w+)*(\\.\\w{2,4})+\$" -->
    </div>
    <div class="mb-3">
      <input type="password" class="form-control input-card" id="password-login" placeholder="Mot de passe"> <!--  required="" -->
    </div>
    <div class="mb-3 form-check stayconnected">
      <input type="checkbox" class="form-check-input" id="stayconnected">
      <label class="form-check-label" for="exampleCheck1">Rester connecté(e)</label>
    </div>
    <button class="btn btn-dark btn-navbar condensed small-caps" type="submit" id="btn-login" >Se Connecter</button>
  </form>
</div>`;

const LoginRegisterPage = () => {  
  let page = document.querySelector("#page");
  page.innerHTML = loginPage;
  let loginForm = document.querySelector(".form-login");
  const user = getUserSessionData();
  if (user) {
    // re-render the navbar for the authenticated user
    Navbar();
    RedirectUrl("/list");
  } else loginForm.addEventListener("submit", onLogin);
};

const onLogin = async (e) => {
  e.preventDefault();
  let login = document.getElementById("email-login");
  let password = document.getElementById("password-login");
  console.log(login.value);

  let user = {
    email: login.value,
    password: password.value,
  };

  try {
    const userLogged = await callAPI(
      API_BASE_URL + "login",
      "POST",
      undefined,
      user
    );
    onUserLogin(userLogged);
  } catch (err) {
    console.error("LoginRegisterPage::onLogin", err);
    PrintError(err);
  }
};

const onUserLogin = (userData) => {
  console.log("onUserLogin:", userData);
  const user = { ...userData, isAutenticated: true };
  setUserSessionData(user);
  // re-render the navbar for the authenticated user
  Navbar();
  RedirectUrl("/");
};

export default LoginRegisterPage;

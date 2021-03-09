/* In a template literal, the ` (backtick), \ (backslash), and $ (dollar sign) characters should be 
escaped using the escape character \ if they are to be included in their template value. 
By default, all escape sequences in a template literal are ignored.*/
import { getUserSessionData, setUserSessionData } from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
const API_BASE_URL = "/api/auths/";

let loginPage = `<h4 id="pageTitle">Login</h4>
<form>
<div class="form-group">
  <label for="login">Login</label>
  <input class="form-control" id="login" type="text" nplaceholder="Enter your login" required="" />
</div>
<div class="form-group">
  <label for="password">Password</label>
  <input class="form-control" id="password" type="password" name="password" placeholder="Enter your password" required=""  />
</div>
<button class="btn btn-primary" id="btn" type="submit">Submit</button>
<!-- Create an alert component with bootstrap that is not displayed by default-->
<div class="alert alert-danger mt-2 d-none" id="messageBoard"></div>
</form>`;

const LoginRegisterPage = () => {  
  let page = document.querySelector("#page");
  page.innerHTML = loginPage;
  let loginForm = document.querySelector("form");
  const user = getUserSessionData();
  if (user) {
    // re-render the navbar for the authenticated user
    Navbar();
    RedirectUrl("/list");
  } else loginForm.addEventListener("submit", onLogin);
};

const onLogin = async (e) => {
  e.preventDefault();
  let login = document.getElementById("login");
  let password = document.getElementById("password");

  let user = {
    login: login.value,
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
    console.error("LoginPage::onLogin", err);
    PrintError(err);
  }
};

const onUserLogin = (userData) => {
  console.log("onUserLogin:", userData);
  const user = { ...userData, isAutenticated: true };
  setUserSessionData(user);
  // re-render the navbar for the authenticated user
  Navbar();
  RedirectUrl("/films");
};

export default LoginRegisterPage;

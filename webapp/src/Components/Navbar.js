let navBar = document.querySelector(".navbar");
import {removeSessionData, currentUser, resetCurrentUser} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
// destructuring assignment
const Navbar = () => {
  let nb;
  let user = currentUser;

  if (user) {
    nb = `
    <h1 class="lines" ></h1>
        <div class= "title">
          <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
          <img class="logo-writing" src="assets/lvs.svg" alt="logo">
        </div>
        <div id="category" class="dropdown">
          <span class="btn btn-secondary dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
            Types de meuble
          </span>
          <button id="voir">
            Voir tous les meubles
          </button>
          <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
            <li><a class="dropdown-item" href="#">Type1</a></li>
            <li><a class="dropdown-item" href="#">Type2</a></li>
            <li><a class="dropdown-item" href="#">Type3</a></li>
          </ul>
        </div>

        <div class="user-head">
          <p class="text-user">Bonjour,</p>
          
          <p id="username" class="text-user">${user.username}</p>
          <div class="dropleft">
            <i id="user" class="bi bi-person-circle dropdown-toggle-user"></i>
            <ul class="dropdown-menu dropdown-menu-left condensed" aria-labelledby="dropdownMenuButton1">
              <li><a class="dropdown-item" id="profile" href="#">Profile</a></li>
              <li><a class="dropdown-item" id="logout" href="#">Se déconnecter</a></li>
            </ul>
          </div>
          <div id="adminToolsIcon">          
          </div>
        </div>
        `;
    navBar.innerHTML = nb;
    if (user.role == "ADMIN"){
      let adminTools = document.getElementById("adminToolsIcon");
      adminTools.innerHTML = `<img src="../assets/key4Admin.png" alt="key" id="keyAdmin" width="30" height="30">`;
      let keyAdmin = document.getElementById("keyAdmin");
      keyAdmin.addEventListener("click", onClickTools);
    }
  } else {
    nb = `<h1 class="lines" ></h1>
    <div class= "title">
      <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
      <img class="logo-writing" src="assets/lvs.svg" alt="logo">
    </div>
    <div id="category" class="dropdown">
      <span class="btn btn-secondary dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
        Types de meuble
      </span>
      <button id="voir">
        Voir tous les meubles
      </button>
      <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
        <li><a class="dropdown-item" href="#">Type1</a></li>
        <li><a class="dropdown-item" href="#">Type2</a></li>
        <li><a class="dropdown-item" href="#">Type3</a></li>
      </ul>
    </div>

    <a class="btn btn-dark btn-navbar condensed small-caps" href="#" data-uri="/login">S'identifier</a>`;
    navBar.innerHTML = nb;
  }

  if (user){
    let logout = document.querySelector("#logout");
    logout.addEventListener("click", onLogout);
  }
  let voir = document.getElementById("voir");
  voir.addEventListener("click", onFurnitureListPage);

};

const onFurnitureListPage = (e) => {
  e.preventDefault;
  RedirectUrl("/furnitures");
};

const onLogout = (e) =>{
  e.preventDefault();
  removeSessionData();
  resetCurrentUser();
  RedirectUrl("/");
  Navbar();
};

const onClickTools = (e) => {
  e.preventDefault();
  RedirectUrl("/confirmRegistration")
}

export default Navbar;

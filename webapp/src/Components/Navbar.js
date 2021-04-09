let navBar = document.querySelector(".navigationbar");
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
          <a id="home" href="#">
            <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
            <img class="logo-writing" src="assets/lvs.svg" alt="logo">
          </a>
        </div>
        <button id="all-furnitures-navbar" class="condensed small-caps">
          Voir tous les meubles
        </button>
        <div id="category" class="dropdown">
          <span class=" dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
            Types de meuble
          </span>
          <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
            <li><a class="dropdown-toggle-item" href="#">Type1</a></li>
            <li><a class="dropdown-toggle-item" href="#">Type2</a></li>
            <li><a class="dropdown-toggle-item" href="#">Type3</a></li>
          </ul>
        </div>

        <div class="user-menu">
        
          <p class="text-user" id="bonjour">Bonjour,</p>
          
          <p id="username-menu" class="text-user">${user.username}</p>
            <div class="dropleft" id="head-menu">
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
      //adminTools.innerHTML = `<i class="bi bi-key-fill" id="keyAdmin" width="30" height="30"></i>`;
      let keyAdmin = document.getElementById("keyAdmin");
      keyAdmin.addEventListener("click", onClickTools);
    }
    else{
      let userMenu = document.querySelector(".user-menu");
      userMenu.style.gridTemplateColumns = "25% 25% 25% 25%";
    }
  } else {
    nb = `<h1 class="lines" ></h1>
    <div class= "title">
      <a id="home" href="#">
        <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
        <img class="logo-writing" src="assets/lvs.svg" alt="logo">
      </a>
    </div>
    <button id="all-furnitures-navbar" class="condensed small-caps">
        Voir tous les meubles
    </button>
    <div id="category" class="dropdown">
      <span class=" dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
        Types de meuble
      </span>
      <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
        <li><a class="dropdown-toggle-item" href="#">Type1</a></li>
        <li><a class="dropdown-toggle-item" href="#">Type2</a></li>
        <li><a class="dropdown-toggle-item" href="#">Type3</a></li>
      </ul>
    </div>

    <a class="btn btn-dark btn-navbar condensed small-caps" href="#" data-uri="/login">S'identifier</a>`;
    navBar.innerHTML = nb;
  }

  if (user){
    let logout = document.querySelector("#logout");
    logout.addEventListener("click", onLogout);
  }
  let voir = document.getElementById("all-furnitures-navbar");
  voir.addEventListener("click", onFurnitureListPage);

  let home = document.getElementById("home");
  home.addEventListener("click", onHomePage);


};

const onFurnitureListPage = (e) => {
  e.preventDefault();
  RedirectUrl("/furnitures");
};
const onHomePage = (e) => {
  e.preventDefault();
  RedirectUrl("/");
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

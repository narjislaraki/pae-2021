import { getUserSessionData} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
import { FurniturePage } from "./FurniturePage.js";
const API_BASE_URL = "api/furnitures/";

let furnitureListTab;



let furnitureListPage = 
`
<button id="bouton">Test</button>
`;

const FurnitureListPage = () => {  
    let page = document.querySelector("#page");
    page.innerHTML = furnitureListPage;
    let button = document.getElementById("bouton");
    button.addEventListener("click", onFurniturePage);
    const user = getUserSessionData();
  };

const onFurniturePage = (e) => {
  console.log("je passe ici");
  FurniturePage(1);
};

  export default FurnitureListPage;
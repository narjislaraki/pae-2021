import { getUserSessionData} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";
const API_BASE_URL = "api/furnitures/";

let furnitureListTab;



let furnitureListPage = 
`

`;

const FurnitureListPage = () => {  
    let page = document.querySelector("#page");
    page.innerHTML = furnitureListPage;
    
    const user = getUserSessionData();
  };


  export default FurnitureListPage;
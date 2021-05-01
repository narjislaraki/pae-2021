import {RedirectUrl} from "./Router.js";
import Navbar from "./Navbar.js";
import {removeSessionData, resetCurrentUser} from "../utils/session.js";

const Logout = () => {
    removeSessionData();
    resetCurrentUser();
    // re-render the navbar for a non-authenticated user
    Navbar();
    RedirectUrl("/login");
};


export default Logout;

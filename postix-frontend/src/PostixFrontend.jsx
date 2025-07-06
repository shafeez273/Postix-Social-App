import {useState} from "react";
import Timeline from "./Timeline.jsx";
import MyProfile from "./MyProfile.jsx";
import Search from "./Search.jsx";
import Login from "./Login.jsx";
import UserProfile from "./UserProfile.jsx";

export default function PostixFrontend() {
    const [auth, setAuth] = useState({name: null, password: null, loggedIn: false});
    const [view, setView] = useState("timeline");
    const [selectedUser, setSelectedUser] = useState(null);
    const [previousView, setPreviousView] = useState("timeline");

    function goToUserProfile(fromView, username) {
        setPreviousView(fromView);
        setSelectedUser(username);
        setView("userprofile");
    }

    return <>
        <header>
            <nav>
                <h2>Postix</h2>
                <ul>
                    <li><a href="#" className={view === "timeline" ? "current" : "default"}
                           onClick={() => setView("timeline")}>Timeline</a></li>
                    <li><a href="#" className={view === "me" ? "current" : "default"}
                           onClick={() => setView("me")}>Me</a></li>
                    <li><a href="#" className={view === "search" ? "current" : "default"}
                           onClick={() => setView("search")}>Search</a></li>
                    <li><a href="#" className={view === "login" ? "current" : "default"}
                           onClick={() => setView("login")}>
                        {auth.loggedIn ? "Log out" : "Log in"}</a></li>
                </ul>
            </nav>
        </header>

        <main>
            {!auth.loggedIn
                ? <Login auth={auth} setAuth={setAuth} setView={setView}/>
                : view === "timeline"
                    ? <Timeline auth={auth} goToUserProfile={goToUserProfile}/>
                    : view === "me"
                        ? <MyProfile auth={auth}/>
                        : view === "search"
                            ? <Search auth={auth} goToUserProfile={goToUserProfile}/>
                            : view === "userprofile" && selectedUser
                                ? <UserProfile auth={auth} username={selectedUser} setView={setView} previousView={previousView}/>
                                : null}
        </main>

        <footer>
            <p><small>Postix, messaging but just better</small></p>
        </footer>

    </>
}
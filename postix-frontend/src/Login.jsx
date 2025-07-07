import {useContext, useRef, useState} from "react";
import {Api} from "./Context.js";
import {basic, anonJson} from "./Headers.js";

export default function Login({auth, setAuth, setView}) {
    const api = useContext(Api);
    const [createAccount, setCreateAccount] = useState(false);
    const name = useRef(undefined);
    const password = useRef(undefined);

    function logOut() {
        fetch(api + "/users/logout", {method: "POST", headers: basic(auth)}).then(response => {
            if (!response.ok) throw new Error(response.statusText);
        }).then(() => {
            setAuth({name: null, password: null, loggedIn: false});
        });
    }

    function logIn() {
        const newAuth = {name: name.current.value, password: password.current.value}
        fetch(api + "/users/login", {method: "POST", headers: basic(newAuth)}).then(response => {
            if (response.ok) return response.json();
            else throw new Error(response.statusText);
        }).then(() => {
            newAuth.loggedIn = true;
            setAuth(newAuth);
            setView("timeline");
        });
    }

    function register() {
        const newAuth = {name: name.current.value, password: password.current.value};
        fetch(api + "/users", {method: "POST", headers: anonJson(),
            body: JSON.stringify(newAuth)}).then(response => {
            if (response.ok) return response.json();
            else throw new Error(response.statusText);
        }).then(() => {
            newAuth.loggedIn = true;
            setAuth(newAuth)
            setView("timeline");
        });
    }

    if (auth.loggedIn) {
        return <>
            <p>Currently logged in as: {auth.name}</p>
            <button onClick={logOut}>Log out</button>
        </>;
    } else {
        return <>
            <p>Currently not logged in.</p>
            <div>
                <input id="new-account" type="checkbox" checked={createAccount}
                       onChange={e => setCreateAccount(e.target.checked)}/>
                <label htmlFor="new-account">I want to create a new account.</label>
            </div>
            <div className="grid">
                <div>
                    <label htmlFor="name">User name:</label>
                    <input id="name" ref={name}/>
                </div>
                <div>
                    <label htmlFor="password">Password:</label>
                    <input type="password" id="password" ref={password}/>
                </div>
            </div>
            {createAccount ? <>
                <button onClick={register}>Register</button>
            </> : <button onClick={logIn}>Log in</button>}
        </>;
    }
}


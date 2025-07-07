import {useContext, useState} from "react";
import {Api} from "./Context.js"
import {basic} from "./Headers.js";


export default function Search({auth, goToUserProfile}) {
    const api = useContext(Api);
    const [query, setQuery] = useState("");
    const [results, setResults] = useState([]);

    function search(query) {
        fetch(api + "/search?query=" + encodeURIComponent(query), {method: "GET", headers: basic(auth)})
        .then(response => {
            if (!response.ok) {throw new Error(response.statusText);}
            else {return response.json();}
        })
        .then(setResults);
    }

    function follow(username) {
        fetch(api+ "/users/" + username + "/followers", {method: "POST", headers: basic(auth)})
            .then(() => {
                setResults(prev => prev.map(user =>
                    user.username === username ? {...user, isFollowing: true} : user
                ));
            });
    }

    function unfollow(username) {
        fetch(api + "/users/" + username + "/followers", {method: "DELETE", headers: basic(auth)})
            .then(() => {
                setResults(prev => prev.map(user =>
                    user.username === username ? {...user, isFollowing: false} : user
                ));
            });
    }

    function viewPosts(username) {
        goToUserProfile("search", username);
    }

    return <>
        <h3>Find Users</h3>
        <form onSubmit={(e) => {
            e.preventDefault();
            search(query);
        }}>
            <input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search username..."/>
            <button>Search</button>
        </form>

        <ul>
            {results.map(user => (
                <li key={user.username}>
                {user.username}{" "}
                    <div className="search-buttons">{user.isFollowing
                        ? <button onClick={() => unfollow(user.username)}>Unfollow</button>
                        : <button onClick={() => follow(user.username)}>Follow</button>}
                    <button onClick={() => viewPosts(user.username)}>View Posts</button>
                    </div>
                </li>
            ))}
        </ul>

        {results.length === 0 && <p>No users found.</p>}
    </>;
}

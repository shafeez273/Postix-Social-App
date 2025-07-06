import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js"
import {basic, basicJson} from "./Headers.js";


export default function UserProfile({auth, username, setView, previousView}) {
    const api = useContext(Api);
    const [posts, setPosts] = useState([]);


    useEffect(() => {
        fetch(api + "/users/" + username + "/posts", {headers: basic(auth)})
        .then(response => {
            if (response.ok) return response.json();
            else throw new Error(response.statusText);
        })
        .then(setPosts)
    }, [auth, api]);

    function likeUnlikePosts(postId, liked) {
        fetch(api + "/posts/" + postId + "/likes", {method: liked ? "DELETE" : "POST", headers: basicJson(auth)})
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText);
            })
            .then(() => {
                loadPosts();
            });
    }

    /*function follow(username) {
        fetch(api+ "/users/" + username + "/followers", {method: "POST", headers: basic(auth)})
        .then(response => {
            if (response.ok) return response.json();
            else throw new Error(response.statusText);
        })
        .then((res) => {
            setResults(prev => prev.map(u =>
                u.username === followingUserName ? {...u, isFollowing: true} : u
            ));
        })

    }

    function unfollow(username) {

    }*/

    return <>
        <h3>{username}</h3>
        {user.isFollowing
            ? <button onClick={() => unfollow(user.username)}>Unfollow</button>
            : <button onClick={() => follow(user.username)}>Follow</button>}
        <button onClick={() => setView(previousView)}>
            Back to {previousView === "search" ? "Search" : "Timeline"}
        </button>
        {posts.map(post => (
            <div key={post.id} className="post">
                <small>{new Date(post.timestamp).toLocaleString()}</small>
                <p>{post.message}</p>
                <button onClick={() => likeUnlikePosts(post.id, post.liked)}>
                    {post.liked ? "Unlike" : "Like"}
                </button>
                <span> ❤️ {post.numberOfLikes}</span>
            </div>
        ))}
        {posts.length === 0 && <p>No posts from this user.</p>}
    </>;
}

import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js";
import {basic, basicJson} from "./Headers.js";

export default function UserProfile({auth, username, setView, previousView}) {
    const api = useContext(Api);
    const [posts, setPosts] = useState([]);
    const [isFollowing, setIsFollowing] = useState(false);
    const [followers, setFollowers] = useState([]);
    const [following, setFollowing] = useState([]);
    const [accessDenied, setAccessDenied] = useState(false);

    function loadPosts() {
        fetch(api + "/users/" + username + "/posts", {headers: basic(auth)})
            .then(res => {
                if (res.ok) return res.json();
                if (res.status === 409) {
                    setAccessDenied(true);
                    return [];
                }
                throw new Error(res.statusText);
            })
            .then(setPosts)
            .catch(err => console.error("Post loading error", err));
    }

    function loadRelations() {
        fetch(api + "/users/" + username + "/followers", {headers: basic(auth)})
            .then(res => res.ok ? res.json() : [])
            .then(setFollowers);

        fetch(api + "/users/" + username + "/following", {headers: basic(auth)})
            .then(res => res.ok ? res.json() : [])
            .then(setFollowing);

        // check if current user is following the profile user
        fetch(api + "/search?query=" + username, {headers: basic(auth)})
            .then(res => res.ok ? res.json() : [])
            .then(results => {
                const match = results.find(u => u.username === username);
                if (match) setIsFollowing(match.isFollowing);
            });
    }

    useEffect(() => {
        loadPosts();
        loadRelations();
    }, [username]);

    function likeUnlikePosts(postId, liked) {
        fetch(api + "/posts/" + postId + "/likes", {
            method: liked ? "DELETE" : "POST",
            headers: basicJson(auth)
        })
            .then(() => {
                setPosts(prev => prev.map(post =>
                    post.id === postId
                        ? {...post, liked: !liked, numberOfLikes: liked ? post.numberOfLikes - 1 : post.numberOfLikes + 1}
                        : post
                ));
            });
    }

    function follow() {
        fetch(api + "/users/" + username + "/followers", {method: "POST", headers: basic(auth)})
            .then(() => {
                setIsFollowing(true);
                loadPosts(); // now allowed to see posts
            });
    }

    function unfollow() {
        fetch(api + "/users/" + username + "/followers", {method: "DELETE", headers: basic(auth)})
            .then(() => {
                setIsFollowing(false);
                setPosts([]); // posts no longer viewable
                setAccessDenied(true);
            });
    }

    return <>
        <button className="back-button" onClick={() => setView(previousView)}>
            Back to {previousView === "search" ? "Search" : "Timeline"}
        </button>

        <h3>{username}'s Profile</h3>

        <p>
            <span>{followers.length} Followers</span>{" | "}
            <span>{following.length} Following</span>{"                 "}

            {auth.name !== username && (
                isFollowing
                    ? <button onClick={unfollow}>Unfollow</button>
                    : <button onClick={follow}>Follow</button>
            )}
        </p>

        {accessDenied ? (
            <p>Can’t view posts if you don’t follow them.</p>
        ) : (
            posts.length > 0 ? posts.map(post => (
                <div key={post.id} className="post">
                    <p>{post.message}</p>
                    <small>{new Date(post.timestamp).toLocaleString()}</small>
                    <button className="post-like" onClick={() => likeUnlikePosts(post.id, post.liked)}>
                        {post.liked ? "Unlike" : "Like"}
                    </button>
                    <span className="post-like-count"> ❤️ {post.numberOfLikes}</span>
                </div>
            )) : <p>No posts from this user.</p>
        )}
    </>;
}

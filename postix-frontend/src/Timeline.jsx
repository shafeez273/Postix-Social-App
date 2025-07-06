import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js"
import {basic, basicJson} from "./Headers.js";

export default function Timeline({auth, goToUserProfile}) {
    const api = useContext(Api);
    const [posts, setPosts] = useState([]);

    function loadPosts() {
        fetch(api + "/timeline", {method: "GET", headers: basic(auth)})
            .then(res => res.ok ? res.json() : [])
            .then(setPosts);
    }

    useEffect(() => {
        loadPosts();
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

    return <>
        <h3>Timeline</h3>
        {posts.map(post => (
            <div key={post.id} className="post">
                <p>
                    <strong>
                        <a href="#" onClick={() => goToUserProfile("timeline", post.username)}>
                            {post.username}
                        </a>
                    </strong>: {post.message}
                </p>
                <small>{new Date(post.timestamp).toLocaleString()}</small><br/>
                <button onClick={() => likeUnlikePosts(post.id, post.liked)}>
                    {post.liked ? "Unlike" : "Like"}
                </button>
                <span> ❤️ {post.numberOfLikes}</span>
            </div>
        ))}
        {posts.length === 0 && <p>No posts yet.</p>}
    </>;
}

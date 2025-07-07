import {useContext, useEffect, useState} from "react";
import {Api} from "./Context.js"
import {basic, basicJson} from "./Headers.js";

export default function Timeline({auth, goToUserProfile}) {
    const api = useContext(Api);
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        fetch(api + "/timeline", {method: "GET", headers: basic(auth)})
        .then(response => {
            if (!response.ok) {throw new Error(response.statusText);}
            else {return response.json();}
        })
        .then(setPosts);
    }, [api]);

    function likeUnlikePosts(postId, liked) {
        fetch(api + "/posts/" + postId + "/likes", {method: liked ? "DELETE" : "POST", headers: basicJson(auth)})
            .then(response => {
                if (!response.ok) throw new Error(response.statusText);
            })
            .then(() => {
                setPosts(prev => prev.map(post => post.id === postId
                            ? {...post, liked: !post.liked, numberOfLikes: liked ? post.numberOfLikes - 1 : post.numberOfLikes + 1}
                            : post)
                );
            });
    }

    return <>
        <h3>Timeline</h3>
        {posts.map(post => (
            <div key={post.id} className="post">
                <p className="post-username"><strong><a href="#" onClick={() => goToUserProfile("timeline", post.username)}>
                    {post.username + "  "}</a></strong></p>
                <p> {post.message} </p>
                <small>{new Date(post.timestamp).toLocaleString()}</small><br/>

                <button className="post-like" onClick={() => likeUnlikePosts(post.id, post.liked)}>
                    {post.liked ? "Unlike" : "Like"}
                </button>
                <span className="post-like-count"> ❤️ {post.numberOfLikes}</span>
            </div>
        ))}

        {posts.length === 0 && <p>No posts.</p>}
    </>;
}

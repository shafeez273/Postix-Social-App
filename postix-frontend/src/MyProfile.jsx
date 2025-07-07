import {useContext, useEffect, useRef, useState} from "react";
import {Api} from "./Context.js";
import {basic, basicJson} from "./Headers.js";

export default function MyProfile({auth}) {
    const api = useContext(Api);
    const [posts, setPosts] = useState([]);
    const newPostMessage = useRef(undefined);
    const [followers, setFollowers] = useState([]);
    const [following, setFollowing] = useState([]);

    useEffect(() => {
        fetch(api + "/users/me/posts", {headers: basic(auth)})
            .then(response => {
                if (!response.ok) {throw new Error(response.statusText);}
                else {return response.json();}
            })
            .then(setPosts);

        fetch(api + "/users/" + auth.name + "/followers", { headers: basic(auth) })
            .then(response => {
                if (!response.ok) {throw new Error(response.statusText);}
                else {return response.json();}
            })            .then(setFollowers);

        fetch(api + "/users/" + auth.name + "/following", { headers: basic(auth) })
            .then(response => {
                if (!response.ok) {throw new Error(response.statusText);}
                else {return response.json();}
            })            .then(setFollowing);
    }, [auth, api]);

    function createPost(e) {
        e.preventDefault();
        const newPost = {message: newPostMessage.current.value.trim()};
        fetch(api + "/posts", {method: "POST",
                headers: basicJson(auth), body: JSON.stringify(newPost)})
        .then(response => {
            if (!response.ok) {throw new Error(response.statusText);}
            else {return response.json();}
        })
        .then(result => {
            setPosts(prev => [result, ...prev]);
            newPostMessage.current.value = "";
        })
    }

    function deletePost(postId) {
        fetch(api + "/posts/" + postId, {method: "DELETE", headers: basic(auth)})
        .then(response  => {
            if (!response.ok) throw new Error(response.statusText);
        })
        .then(() => {
            setPosts(posts.filter(p => p.id !== postId));
        });
    }

    return <>
        <h3>Me</h3>
        <p>
            <span>{followers.length} Followers</span>{" | "}
            <span>{following.length} Following</span>{"                 "}
        </p>

        <form onSubmit={createPost}>
            <textarea ref={newPostMessage} placeholder="Write something..."/>
            <button type="submit">Post</button>
        </form>
        {
            posts.map(post => (
                <div key={post.id} className="post">
                    <p>{post.message}</p>
                    <small>{new Date(post.timestamp).toLocaleString()}</small><br/>
                    <span className="post-like-count"> ❤️ {post.numberOfLikes}</span>
                    <button onClick={() => deletePost(post.id)}>Delete</button>
                </div>
            ))
        }
        {
            posts.length === 0 && <p>You haven't posted anything yet.</p>
        }
    </>
        ;
}

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Tweet from "../components/Tweet";

export default function Home() {
    const [tweets, setTweets] = useState([]);
    const [newTweet, setNewTweet] = useState("");
    const navigate = useNavigate();

    const userId = localStorage.getItem("userId");
    const username = localStorage.getItem("username");

    useEffect(() => {
        if (!userId) navigate("/");
    }, [userId, navigate]);

    const fetchTweets = async () => {
        const res = await fetch("http://localhost:3000/tweet/all", {
            credentials: "include",
        });
        const data = await res.json();
        if (Array.isArray(data)) setTweets(data.reverse());
    };


    useEffect(() => {
        fetchTweets();
    }, []);

    const handleLogout = () => {
        localStorage.clear();
        navigate("/");
    };

    const handlePostTweet = async (e) => {
        e.preventDefault();
        if (!newTweet.trim()) return;

        await fetch("http://localhost:3000/tweet", {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                content: newTweet.trim(),
                userId: Number(userId),
            }),
        });


        setNewTweet("");
        fetchTweets();
    };

    // ✅ SİL
    const handleDelete = async (tweetId) => {
        if (!tweetId) return;
        if (!window.confirm("Bu tweeti silmek istediğine emin misin?")) return;

        const url = `http://localhost:3000/tweet/${tweetId}?userId=${Number(userId)}`;
        const res = await fetch(url, {
            method: "DELETE",
            credentials: "include",
        });

        if (res.ok) fetchTweets();
        else alert(`Silme başarısız! Status: ${res.status}`);
    };

    // ✅ GÜNCELLE
    const handleUpdate = async (tweetId, oldContent) => {
        const newText = prompt("Tweeti düzenle:", oldContent);
        if (!newText || newText.trim() === oldContent) return;

        const res = await fetch(`http://localhost:3000/tweet/${tweetId}`, {
            method: "PUT",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                content: newText.trim(),
                userId: Number(userId),
            }),
        });


        if (res.ok) fetchTweets();
        else alert(`Güncelleme başarısız! Status: ${res.status}`);
    };

    return (
        <div style={{ maxWidth: 600, margin: "0 auto", padding: 20, minHeight: "100vh" }}>
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginBottom: 20,
                    paddingBottom: 15,
                    borderBottom: "1px solid #e1e8ed",
                }}
            >
                <div>
                    <h2 style={{ color: "#14171a", margin: 0, fontSize: "20px", fontWeight: "bold" }}>Anasayfa</h2>
                    {username && <small style={{ color: "#657786", fontSize: "14px", display: "block", marginTop: "4px" }}>@{username}</small>}
                </div>

                <button
                    onClick={handleLogout}
                    style={{
                        background: "white",
                        color: "#E0245E",
                        border: "1px solid #E0245E",
                        borderRadius: 20,
                        padding: "8px 16px",
                        cursor: "pointer",
                        fontWeight: "bold",
                        fontSize: "14px",
                        transition: "background-color 0.2s",
                    }}
                    onMouseEnter={(e) => { e.currentTarget.style.backgroundColor = "#fef0f3"; }}
                    onMouseLeave={(e) => { e.currentTarget.style.backgroundColor = "white"; }}
                >
                    Çıkış Yap
                </button>
            </div>

            <form onSubmit={handlePostTweet} style={{ marginBottom: 20, padding: "15px", backgroundColor: "#f7f9fa", borderRadius: "12px", border: "1px solid #e1e8ed" }}>
                <textarea
                    value={newTweet}
                    onChange={(e) => setNewTweet(e.target.value)}
                    placeholder="Neler oluyor?"
                    maxLength={280}
                    style={{
                        width: "100%",
                        minHeight: 100,
                        borderRadius: 10,
                        padding: 15,
                        border: "1px solid #e1e8ed",
                        resize: "vertical",
                        fontSize: "15px",
                        fontFamily: "inherit",
                        backgroundColor: "#fff",
                        color: "#14171a",
                    }}
                />
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginTop: 10 }}>
                    <span style={{ color: "#657786", fontSize: "13px" }}>
                        {newTweet.length}/280
                    </span>
                    <button
                        type="submit"
                        disabled={!newTweet.trim()}
                        style={{
                            backgroundColor: newTweet.trim() ? "#1DA1F2" : "#8ed0f9",
                            color: "white",
                            border: "none",
                            borderRadius: 20,
                            padding: "10px 24px",
                            cursor: newTweet.trim() ? "pointer" : "not-allowed",
                            fontWeight: "bold",
                            fontSize: "15px",
                            transition: "background-color 0.2s",
                        }}
                    >
                        Tweetle
                    </button>
                </div>
            </form>

            {tweets.map((t) => (
                <Tweet
                    key={t.id}
                    tweet={t}
                    currentUserId={Number(userId)}
                    onDelete={() => handleDelete(t.id)}
                    onUpdate={() => handleUpdate(t.id, t.content)}
                    onRefresh={fetchTweets}
                />
            ))}
        </div>
    );
}

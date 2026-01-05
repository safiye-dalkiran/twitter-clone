import { useMemo, useState, useEffect } from "react";
import Comment from "./Comment";

export default function Tweet({ tweet, currentUserId, onDelete, onUpdate, onRefresh }) {
    const API_BASE = "http://localhost:3000";
    
    // Backend endpoint'leri kontrol edin:
    // Like için yaygın formatlar: /like, /tweet/like, /api/like, /tweet/{id}/like
    // Retweet için yaygın formatlar: /retweet, /tweet/retweet, /api/retweet, /tweet/{id}/retweet

    // Tweet sahibini güvenli çek
    const tweetOwnerId =
        tweet?.userId ??
        tweet?.user?.id ??
        tweet?.user?.userId ??
        tweet?.ownerId ??
        tweet?.accountId ??
        null;

    const isOwner = String(tweetOwnerId) === String(currentUserId);

    const [showComments, setShowComments] = useState(false);
    const [likes, setLikes] = useState(tweet.likes || []);
    const [retweets, setRetweets] = useState(tweet.retweets || []);
    const [comments, setComments] = useState(tweet.comments || []);
    const [newComment, setNewComment] = useState("");

    // Tweet prop değiştiğinde state'leri güncelle
    useEffect(() => {
        setLikes(tweet.likes || []);
        setRetweets(tweet.retweets || []);
        setComments(tweet.comments || []);
    }, [tweet]);

    // Comments fetch (eğer tweet içinde yoksa)
    const fetchComments = async () => {
        try {
            const res = await fetch(`${API_BASE}/comment/tweet/${tweet.id}`);
            if (res.ok) {
                const data = await res.json();
                if (Array.isArray(data)) {
                    setComments(data);
                }
            }
        } catch (err) {
            console.error("Comments fetch hatası:", err);
        }
    };

    // Comments gösterildiğinde fetch et
    useEffect(() => {
        if (showComments && (!comments || comments.length === 0)) {
            fetchComments();
        }
    }, [showComments]);

    // likes/retweets içinden userId'yi güvenli çek
    const extractUserId = (x) => {
        if (x == null) return null;
        if (typeof x === "number" || typeof x === "string") return String(x);

        return String(
            x.userId ??
            x.user?.id ??
            x.user?.userId ??
            x.accountId ??
            x.ownerId ??
            null
        );
    };

    const isLiked = useMemo(
        () => likes.some((l) => extractUserId(l) === String(currentUserId)),
        [likes, currentUserId]
    );

    const isRetweeted = useMemo(
        () => retweets.some((r) => extractUserId(r) === String(currentUserId)),
        [retweets, currentUserId]
    );

    // Like toggle
    const handleLikeToggle = async (e) => {
        e?.preventDefault();
        e?.stopPropagation();
        
        const endpoint = isLiked ? "dislike" : "like";
        const url = `${API_BASE}/${endpoint}`;
        const requestBody = { userId: Number(currentUserId), tweetId: Number(tweet.id) };

        console.log("Like request:", url, requestBody);

        try {
            const res = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });

            const raw = await res.text();
            let payload = null;
            try { payload = JSON.parse(raw); } catch { }

            console.log("Like response:", res.status, payload || raw);

            if (!res.ok) {
                console.error("Like hata:", res.status, payload || raw);
                
                // Backend "zaten beğendiniz" hatası veriyorsa, state'i senkronize et
                if (res.status === 400 && payload?.message?.includes("zaten beğendiniz")) {
                    // State zaten doğru, sadece refresh yap
                    onRefresh?.();
                    return;
                }
                
                // Diğer hatalar için refresh yap (state'i backend'den güncelle)
                if (res.status >= 400 && res.status < 500) {
                    onRefresh?.();
                }
                return;
            }

            if (isLiked) {
                setLikes((prev) => prev.filter((l) => extractUserId(l) !== String(currentUserId)));
            } else {
                setLikes((prev) => [...prev, { userId: Number(currentUserId) }]);
            }

            onRefresh?.();
        } catch (err) {
            console.error("Like network hatası:", err);
        }
    };

    // Retweet toggle
    const handleRetweetToggle = async (e) => {
        e?.preventDefault();
        e?.stopPropagation();
        
        const url = `${API_BASE}/retweet`;
        const requestBody = { userId: Number(currentUserId), tweetId: Number(tweet.id) };

        console.log("Retweet request:", url, requestBody);

        try {
            const res = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });

            const raw = await res.text();
            let payload = null;
            try { payload = JSON.parse(raw); } catch { }

            console.log("Retweet response:", res.status, payload || raw);

            if (!res.ok) {
                console.error("Retweet hata:", res.status, payload || raw);
                
                // Backend benzer hatalar için state'i senkronize et
                if (res.status === 400) {
                    onRefresh?.();
                }
                return;
            }

            if (!isRetweeted) {
                setRetweets((prev) => [...prev, { userId: Number(currentUserId) }]);
            }

            onRefresh?.();
        } catch (err) {
            console.error("Retweet network hatası:", err);
        }
    };

    // Comment add
    const handleAddComment = async (e) => {
        e.preventDefault();
        if (!newComment.trim()) return;

        const res = await fetch(`${API_BASE}/comment`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                content: newComment.trim(),
                userId: Number(currentUserId),
                tweetId: Number(tweet.id),
            }),
        });

        if (!res.ok) {
            console.error("Comment ekleme hata:", res.status, await res.text());
            return;
        }

        const added = await res.json();
        setComments((prev) => [...prev, added]);
        setNewComment("");
        fetchComments();
        onRefresh?.();
    };

    // Comment delete
    const handleDeleteComment = async (commentId) => {
        if (!window.confirm("Yorum silinsin mi?")) return;

        const res = await fetch(`${API_BASE}/comment/${commentId}?userId=${Number(currentUserId)}`, {
            method: "DELETE",
        });

        if (res.ok) {
            setComments((prev) => prev.filter((c) => c.id !== commentId));
            fetchComments();
            onRefresh?.();
        } else {
            console.error("Comment silme hata:", res.status, await res.text());
        }
    };

    // Tarih formatlama
    const formatDate = (dateString) => {
        if (!dateString) return "";
        const date = new Date(dateString);
        const now = new Date();
        const diffInSeconds = Math.floor((now - date) / 1000);
        const diffInMinutes = Math.floor(diffInSeconds / 60);
        const diffInHours = Math.floor(diffInMinutes / 60);
        const diffInDays = Math.floor(diffInHours / 24);

        if (diffInSeconds < 60) return "Az önce";
        if (diffInMinutes < 60) return `${diffInMinutes} dk`;
        if (diffInHours < 24) return `${diffInHours} saat`;
        if (diffInDays < 7) return `${diffInDays} gün`;
        return date.toLocaleDateString("tr-TR", { day: "numeric", month: "short", year: date.getFullYear() !== now.getFullYear() ? "numeric" : undefined });
    };

    return (
        <div style={{ borderBottom: "1px solid #e1e8ed", padding: 15, backgroundColor: "#fff", marginBottom: 10, borderRadius: 10, transition: "background-color 0.2s" }}
            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = "#f7f9fa"}
            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = "#fff"}
        >
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: 8 }}>
                <div>
                    <strong style={{ color: "#14171a", fontSize: "15px" }}>@{tweet.username || tweet.user?.username || "bilinmeyen"}</strong>
                    {tweet.createdAt && (
                        <span style={{ color: "#657786", fontSize: "14px", marginLeft: "8px" }}>
                            · {formatDate(tweet.createdAt)}
                        </span>
                    )}
                </div>
            </div>
            <p style={{ color: "#14171a", fontSize: "15px", lineHeight: "1.5", marginBottom: 12, whiteSpace: "pre-wrap", wordBreak: "break-word" }}>
                {tweet.content}
            </p>

            <div style={{ marginTop: 10, display: "flex", gap: 30, alignItems: "center", paddingTop: 10, borderTop: "1px solid #f0f0f0" }}>
                <button
                    type="button"
                    onClick={handleLikeToggle}
                    style={{
                        cursor: "pointer",
                        color: isLiked ? "#E0245E" : "#657786",
                        background: "none",
                        border: "none",
                        fontWeight: isLiked ? "bold" : "normal",
                        fontSize: "14px",
                        display: "flex",
                        alignItems: "center",
                        gap: "5px",
                        padding: "5px 10px",
                        borderRadius: "20px",
                        transition: "background-color 0.2s",
                    }}
                    onMouseEnter={(e) => { e.currentTarget.style.backgroundColor = isLiked ? "#fdf2f5" : "#f7f9fa"; }}
                    onMouseLeave={(e) => { e.currentTarget.style.backgroundColor = "transparent"; }}
                >
                    {isLiked ? "❤️" : "🤍"} {likes.length}
                </button>

                <button
                    type="button"
                    onClick={handleRetweetToggle}
                    style={{
                        cursor: "pointer",
                        color: isRetweeted ? "#17bf63" : "#657786",
                        background: "none",
                        border: "none",
                        fontSize: "14px",
                        display: "flex",
                        alignItems: "center",
                        gap: "5px",
                        padding: "5px 10px",
                        borderRadius: "20px",
                        transition: "background-color 0.2s",
                    }}
                    onMouseEnter={(e) => { e.currentTarget.style.backgroundColor = isRetweeted ? "#f0fdf4" : "#f7f9fa"; }}
                    onMouseLeave={(e) => { e.currentTarget.style.backgroundColor = "transparent"; }}
                >
                    {isRetweeted ? "🔄" : "🔁"} {retweets.length}
                </button>

                <button
                    type="button"
                    onClick={() => setShowComments((s) => !s)}
                    style={{
                        cursor: "pointer",
                        color: showComments ? "#1DA1F2" : "#657786",
                        background: "none",
                        border: "none",
                        fontSize: "14px",
                        display: "flex",
                        alignItems: "center",
                        gap: "5px",
                        padding: "5px 10px",
                        borderRadius: "20px",
                        transition: "background-color 0.2s",
                    }}
                    onMouseEnter={(e) => { e.currentTarget.style.backgroundColor = "#f0f8ff"; }}
                    onMouseLeave={(e) => { e.currentTarget.style.backgroundColor = "transparent"; }}
                >
                    💬 {comments.length}
                </button>

                {isOwner && (
                    <div style={{ marginLeft: "auto", display: "flex", gap: "10px" }}>
                        <button
                            type="button"
                            onClick={onUpdate}
                            style={{
                                color: "#1DA1F2",
                                border: "none",
                                background: "none",
                                cursor: "pointer",
                                fontSize: "14px",
                                padding: "5px 10px",
                                borderRadius: "20px",
                                transition: "background-color 0.2s",
                            }}
                            onMouseEnter={(e) => { e.currentTarget.style.backgroundColor = "#f0f8ff"; }}
                            onMouseLeave={(e) => { e.currentTarget.style.backgroundColor = "transparent"; }}
                        >
                            Düzenle
                        </button>
                        <button
                            type="button"
                            onClick={onDelete}
                            style={{
                                color: "#E0245E",
                                border: "none",
                                background: "none",
                                cursor: "pointer",
                                fontSize: "14px",
                                padding: "5px 10px",
                                borderRadius: "20px",
                                transition: "background-color 0.2s",
                            }}
                            onMouseEnter={(e) => { e.currentTarget.style.backgroundColor = "#fdf2f5"; }}
                            onMouseLeave={(e) => { e.currentTarget.style.backgroundColor = "transparent"; }}
                        >
                            Sil
                        </button>
                    </div>
                )}
            </div>

            {showComments && (
                <div style={{ marginTop: 15, paddingLeft: 20, borderLeft: "2px solid #e1e8ed" }}>
                    <form onSubmit={handleAddComment} style={{ marginBottom: 15, display: "flex", gap: 8 }}>
                        <input
                            type="text"
                            value={newComment}
                            onChange={(e) => setNewComment(e.target.value)}
                            placeholder="Yorum yap..."
                            style={{
                                flex: 1,
                                borderRadius: 20,
                                border: "1px solid #e1e8ed",
                                padding: "8px 15px",
                                fontSize: "14px",
                                backgroundColor: "#f7f9fa",
                            }}
                        />
                        <button
                            type="submit"
                            disabled={!newComment.trim()}
                            style={{
                                borderRadius: 20,
                                border: "none",
                                backgroundColor: newComment.trim() ? "#1DA1F2" : "#8ed0f9",
                                color: "white",
                                padding: "8px 20px",
                                cursor: newComment.trim() ? "pointer" : "not-allowed",
                                fontWeight: "bold",
                                fontSize: "14px",
                            }}
                        >
                            Gönder
                        </button>
                    </form>

                    {comments.map((c) => (
                        <Comment key={c.id} comment={c} currentUserId={currentUserId} onDelete={handleDeleteComment} />
                    ))}
                </div>
            )}
        </div>
    );
}

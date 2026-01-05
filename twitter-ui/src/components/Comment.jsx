export default function Comment({ comment, currentUserId, onDelete }) {
    // Yorumun sahibi miyiz kontrolü
    const isOwner = String(comment.userId) === String(currentUserId);

    return (
        <div style={{ 
            padding: '8px 15px', 
            borderLeft: '2px solid #1DA1F2', 
            backgroundColor: '#f8f9fa', 
            marginBottom: '5px',
            fontSize: '14px',
            borderRadius: '0 8px 8px 0'
        }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <strong>@{comment.username}</strong>
                {isOwner && (
                    <button 
                        onClick={() => onDelete(comment.id)} 
                        style={{ color: 'red', border: 'none', background: 'none', cursor: 'pointer', fontSize: '12px' }}
                    >
                        Sil
                    </button>
                )}
            </div>
            <p style={{ margin: '4px 0', color: '#333' }}>{comment.content}</p>
            <small style={{ color: '#657786', fontSize: '11px' }}>
                {new Date(comment.createdAt).toLocaleString('tr-TR')}
            </small>
        </div>
    );
}
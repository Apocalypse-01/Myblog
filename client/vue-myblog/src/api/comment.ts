import _axios from "@/utils/request.js"

export async function addComment(commentVO: { articleId: number; userId: number; userName: string; content: string }) {
    const re = await _axios.post("/comment/add", commentVO)
    return re
}

export async function getCommentList(articleId: number) {
    const re = await _axios.get("/comment/list", {
        params: { articleId }
    })
    return re
}

export async function deleteComment(id: number, userId: number) {
    const re = await _axios.delete("/comment/delete", {
        params: { id, userId }
    })
    return re
}

export async function getCommentCount(articleId: number) {
    const re = await _axios.get("/comment/count", {
        params: { articleId }
    })
    return re
}

import _axios from '@/utils/request.js'

export async function aiAssist(action: string, content: string, title?: string) {
    const re = await _axios.post('/ai/assist', {
        action: action,
        content: content,
        title: title || ''
    })
    return re
}

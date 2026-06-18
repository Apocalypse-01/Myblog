import _axios from '@/utils/request.js'
import {Article} from '@/class/class.ts'

//文章信息 传输
export async function getArticleDetali(articleId:number){

    const re = await _axios.post('/article/getDetails',
        {
            // 传递的参数
            articleId:articleId,

            
        })
        
    return re

}
/**
 * 发表文章
 * @param article 
 * @returns 
 */
export async function releaseArticle(article:Article){
        
    const re = await  _axios.put('/article/release',{
            
            articleTitle:article.getArticleTitle(),
            articleSign:article.getAaricleSign(),
            articleContent:article.getarticleContent(),
            authorId:article.authorId,
            authorName:article.authorName
            
        }
    ) 
    return re
}

/**
 * 根据作者,获取文章列表  
 * @param authorId 
 * @param currentPage 
 * @returns 
 */
export async function getArticleListById(authorId:number,currentPage:number) {
    
    const re =   await _axios.get("/article/getArticlePage",{
        params:{
            authorId:authorId,
            currentPage:currentPage
        }
    })
    
    return re

}

/**
 * 根据 标签 和 顺序(倒叙和顺序) 获取文章列表
 * @param articleSign 
 * @param order 
 * @returns 
 */
export async function getArticleListBySign(articleSign:number,order:number,currentPage:number) {
    
    const re =   await _axios.get("/article/getAllArticlePage",{
        
        params:{
            articleSign:articleSign,
            order:order,
            currentPage:currentPage
        }
    })
    
    return re

}
/**
 * 获得文章详细内容
 * @param articleId 
 * @returns 
 */
export async function getArticle(articleId:number){
    const re =   await _axios.get("/article/getArticle",{

            params:{
                    
                articleId:articleId 

            }
        })

    return re

}
/**
 * 获得文章评论列表
 * @param articleId 
 * @returns 
 */
export async function getArticleComment(articleId:number){
    const re =   await _axios.get("/article/getArticleComment",{
            
            params:{
                articleId:articleId,

            }
        })

    return re

}


/**
 * 获取 收藏夹的文章
 * @param articleId
 * @returns 
 */
export async function getArticleFavirotes(authorId:number,currentPage:number){
    const re =   await _axios.get("/article/getArticleFavorites",{

            params:{
                authorId:authorId ,
                currentPage:currentPage

            }
        })

    return re

}

/**
 * 获取 点赞 的文章
 * @param authorId 
 * @param currentPage 
 * @returns 
 */
export async function getArticlelikes(authorId:number,currentPage:number){
    const re =   await _axios.get("/article/getArticleLikes",{
        
            params:{
                authorId:authorId ,
                currentPage:currentPage

            }
        })

    return re

}

//点赞
export async function articleYesLike(articleId:number,authorId:number,authorName:string){
    const re =   await _axios.get("/article/articleInsertLikes",{
            
            params:{
                articleId:articleId,
                authorId:authorId,
                authorName:authorName
                
            }
        })

    return re

}

// 取消点赞
export async function articleNoLike(articleId:number,authorId:number,authorName:string){
    const re =   await _axios.delete("/article/articleCancelLikes",{

            params:{
                articleId:articleId,
                authorId:authorId,
                authorName:authorName                
            }
        })

    return re

}
// 收藏
export async function articleYesFavorites(articleId:number,authorId:number,authorName:string){
    const re =   await _axios.get("/article/articleInsertFavorites",{
            
            params:{
                articleId:articleId,
                authorId:authorId,
                authorName:authorName
                
            }
        })

    return re

}


// 取消 收藏
export async function articleNoFavorites(articleId:number,authorId:number,authorName:string){
    const re =   await _axios.delete("/article/articleCancelFavorites",{

            params:{
                articleId:articleId,
                authorId:authorId,
                authorName:authorName                
            }
        })

    return re

}

// 搜索文章
export async function searchArticle(keyword:string,currentPage:number){
    const re = await _axios.get("/article/search",{
        params:{
            keyword:keyword,
            currentPage:currentPage
        }
    })

    return re
}

// 编辑文章
export async function updateArticle(article: { id: number; articleTitle: string; articleSign: string; articleContent: string; authorId: number }){
    const re = await _axios.put("/article/update", article)
    return re
}

// 删除文章
export async function deleteArticle(id:number, authorId:number){
    const re = await _axios.delete("/article/delete",{
        params:{
            id:id,
            authorId:authorId
        }
    })
    return re
}

// 查询是否已点赞
export async function isLiked(articleId:number, authorId:number){
    const re = await _axios.get("/article/isLiked",{
        params:{ articleId, authorId }
    })
    return re
}

// 查询是否已收藏
export async function isFavorited(articleId:number, authorId:number){
    const re = await _axios.get("/article/isFavorited",{
        params:{ articleId, authorId }
    })
    return re
}

export async function getAllByAuthor(authorId:number){
    const re = await _axios.get("/article/getAllByAuthor",{
        params:{ authorId }
    })
    return re
}

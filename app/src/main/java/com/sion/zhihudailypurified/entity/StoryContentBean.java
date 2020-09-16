package com.sion.zhihudailypurified.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sion.zhihudailypurified.db.ListJsonConverter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kotlinx.android.parcel.Parcelize;

@Entity
@TypeConverters(ListJsonConverter.class)
@Parcelize
public class StoryContentBean {

    /**
     * body : <div class="main-wrap content-wrap">
     * <div class="headline">
     *
     * <div class="img-place-holder"></div>
     *
     *
     *
     * </div>
     *
     * <div class="content-inner">
     *
     *
     *
     *
     * <div class="question">
     * <h2 class="question-title">如何看待原油期货结算价持续刷新新低，历史上首次收于负值？</h2>
     *
     * <div class="answer">
     *
     * <div class="meta">
     * <img class="avatar" src="http://pic1.zhimg.com/da8e974dc_is.jpg">
     * <span class="author">Mr.X，</span><span class="bio">经济 政治 金融的瘾君子</span>
     * <a href="https://www.zhihu.com/question/388723046/answer/1168381070" class="originUrl" hidden>查看知乎原文</a>
     * </div>
     *
     * <div class="content">
     * <p>石油价格破天荒的一度跌倒 -40$，这种卖东西倒贴钱看似荒诞的事情，其实只要了解来龙去脉，就会觉得其实也挺合理的。仔细想一想，这和大萧条时期倾倒牛奶很相似，都是市场在 1.供大于求 2. 存在减产成本 3. 存在交易、存储成本， 做出的理性选择。当然这次石油（WTI）的负价格是其期货的价格，这和原油期货市场的一些动态也相关。</p>
     * <p><strong>石油市场供求不平衡</strong></p>
     * <p>近期的原油市场经历着来自需求方和供给方的双重打击，新冠疫情带来的经济社会活动的停摆使得 3-4 月的石油消耗大幅下跌，IEA 估计 4 月石油消费跌 29mb/d，高盛的估计是 26mb/d。这意味着超过 1/4 的原油消费在短期内就蒸发了（全球的石油消费在 100mb/d 的水平）。</p>
     * <p><a class=" wrap external" href="http://link.zhihu.com/?target=https%3A//www.iea.org/reports/oil-market-report-april-2020" target="_blank" rel="nofollow noreferrer">Oil Market Report - April 2020 – Analysis - IEA</a></p>
     * <p>同时，三月初沙特和俄罗斯维持了近 3 年的 OPEC+ 体系分崩离析，不仅减产协议没有达成，两者更是打响了价格战，通过增产来争夺市场份额。如此，3 月 - 4 月原油市场出现了供给远远大于需求的状况，大概相当于 20-30mb/d 的产量不能被消化，不得不进入油罐里存储起来。</p>
     * <p>值得一提的是最近才达成的新 OPEC+ 的减产协议，总规模达到近 10mb/d，但是 1. 协议要到 5-6 月才能生效，所谓远水解不了近渴，2. 鉴于减产量占 OPEC+ 总产量的 1/3 之多，协议的执行度还令人存疑，3. 新冠疫情带来的需求缩减之大，仅仅靠 OPEC+ 减产并不能把市场拉回平衡，而 OPEC+ 之外，包括美国的石油减产协议则是雷声大雨点小，实行起来困难重重。</p>
     * <p><strong>石油生产、储存成本和石油价格的关系</strong></p>
     * <p>仅仅是供大于求并不能直接导致石油价格变负，石油生产商完全可以选择不生产。赔本的买卖不做还不行吗？但其实负的石油价格早在 3 月底在美国就出现了，不同的是当时的负价格是井口价格（wellhead price），而今天的负价格是 WTI 石油期货价格。</p>
     * <p>回顾一下经济学里关于生产成本和价格的关系：如果市场是完全竞争市场，那么市场价格应该等于边际生产厂商的边际生产成本。今天的原油市场，OPEC+ 不再履行自己作为垄断寡头的职责，所有厂商都在给定的价格上最大化自己的产量，所以接近一个完全竞争市场。短期内，市场的价格也应该是由边际厂商的短期生产成本作为支撑点。这一生产成本就是页岩油（边际厂商）的现金成本（短期生产成本）。具体可以参考我之前关于页岩油的成本分析。现今这一成本估计在$20+ 左右。</p>
     * <p><a class="internal" href="https://www.zhihu.com/question/20961822/answer/136375344">美国页岩油的生产成本大概是多少？</a></p>
     * <p>这一成本能成为价格支撑点的逻辑是：当价格低于这一成本时，厂商可以选择继续生产，但是暂时把油存储起来（在油罐里）而不卖掉，也可以选择停止生产（相当于把石油存储在地下）。如此减少了市场上石油的供给，让市场价格回到成本价上。但是，当油的存储成本很高，同时停产费用也很高的情况下，生产成本就不再是价格支撑点，最合理的方法往往是把油继续开采出来，以低于成本的价格，甚至是负的价格卖掉。例如，当生产成本是 20$，但是存储成本很高，或者完全找不到存储空间，并且如果把生产停掉会造成额外的 25$的成本，那么继续生产，并以 -1$的价格卖掉就是一个合理的选择了。</p>
     * <p>而对负价卖油的油井来说，这就是当下的现实：一方面近几个月的大量剩余原油的积累使得石油可用存储空间急剧减少，额外的存储成本也变得很高，尤其在美国。另一方面，页岩油主动停产（shut in）往往需要进行降压的处理，而如果之后要复工，又要加压，平均每口井都要几百万美元的成本。所以很多井都选择了宁愿负价出售，也不停工。</p>
     * <p>但是这种低于现金成本的价格并不能维持很长时间，因为页岩油的另一种减产方法是不再开新井，让老油井的产量自然下降。这种减产和 shut in 不一样，不需要额外的成本，而我们从钻井平台数的断崖是下跌就可以看出这种减产已经在进行了（见下图）。</p>
     * <figure><img class="content-image" src="https://pic4.zhimg.com/v2-1fb86af6da67832d3daf521bf5598827_b.jpg" alt=""></figure><p>同样的逻辑也可以解释为什么大萧条时间，奶农宁愿倾倒牛奶也不愿意停产：一方面存储牛奶的成本很高，另一方面让奶牛停止产奶的成本也很高（奶牛会因此生病）。而如果石油可以像牛奶一样倾泻到河流里不产生污染的话，油厂也会选择这样做的吧。</p>
     * <p><strong>石油期货价格</strong></p>
     * <p>最后来说一说石油期货，这次跌倒 -40$ 的是 WTI 五月交割的期货的价格，可以看到更后期交割的 WTI 期货的价格还是维持在 20$ 左右（见下图）。同时，另一石油标的布伦特油的价格在 25$ 以上，远没有达到负数。</p>
     * <figure><img class="content-image" src="https://pic2.zhimg.com/v2-d0977b829d6e2d9d976f925f5d0c49dd_b.jpg" alt=""></figure><p>所以我的解读是这一价格的大跌，是持有是 WTI 五月交割合约的多头的一方不想实物交割（明天是合约到期日），因为找不到足够的存储空间来接受石油，或者存储空间的成本太高，所以急于平仓，火速卖期货触发了开始的大跌。而一些追寻动势策略的投资，例如 CTAs，期权对冲等把跌势进一步放大。</p>
     * <p>从这个角度，这次石油大跌反映的不是石油商品的实物供需的基本面变化，更多的是期货市场本身技术性的波动。当然这两者是高度相关的，很多的产油商都通过期货来对冲价格风险，早在一年半载前就锁定现在的卖出价了。所以即使现在的现货市场价格如此之低，也不妨碍他们保持产量。最好的例子就是墨西哥，在最近的 OPEC+ 的协议上，他们是唯一一个不参加减产的国家，因为据说他们今年的产量已经有一般以上对冲过了。这就会进一步加剧供大于求和储油空间不足，让短期内的油价跌幅更大。</p>
     * <p>此外，更长期的 WTI 期货的还是维持在 20$ 以上，一定程度上印证了中短期页岩油的现金成本（20$+）是油价的支撑点。同时布伦特油和 WTI 的差价最近一直在扩大，反映了作为海运油的布伦特油在运输和存储上的成本优势。</p>
     * </div>
     * </div>
     *
     *
     * <div class="view-more"><a href="https://www.zhihu.com/question/388723046">查看知乎讨论<span class="js-question-holder"></span></a></div>
     *
     * </div>
     *
     *
     * </div>
     * </div><script type=“text/javascript”>window.daily=true</script>
     * image_hue : 0xb39059
     * image_source : SashSegal / CC0
     * title : 原油价格为什么会跌到变负值？
     * url : https://daily.zhihu.com/story/9722986
     * image : https://pic2.zhimg.com/v2-57e94f61938e15bd9d44e4365b0cdabd.jpg
     * share_url : http://daily.zhihu.com/story/9722986
     * js : []
     * ga_prefix : 042207
     * images : ["https://pic1.zhimg.com/v2-ca9e178d7a4b085d5f48b40a87a3596c.jpg"]
     * type : 0
     * id : 9722986
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"]
     */

    private String body;
    private String image_hue;
    private String image_source;
    private String title;
    private String url;
    private String image;
    private String share_url;
    private String ga_prefix;
    private int type;
    @PrimaryKey
    private int id;
    private List<String> js;
    private List<String> images;
    private List<String> css;
    @Embedded
    private StoryContentExtraBean extra;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_hue() {
        return image_hue;
    }

    public void setImage_hue(String image_hue) {
        this.image_hue = image_hue;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getJs() {
        if (js == null) {
            return new ArrayList<>();
        }
        return js;
    }

    public void setJs(List<String> js) {
        this.js = js;
    }

    public List<String> getImages() {
        if (images == null) {
            return new ArrayList<>();
        }
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getCss() {
        if (css == null) {
            return new ArrayList<>();
        }
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    @NotNull
    public StoryContentExtraBean getExtra() {
        return extra;
    }

    public void setExtra(@NotNull StoryContentExtraBean extra) {
        this.extra = extra;
    }
}
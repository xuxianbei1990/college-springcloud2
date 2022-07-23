package college.springcloud.student.controller.pdf;

import lombok.Data;

/**
 * 一件代发合同
 *
 * @author: xuxianbei
 * Date: 2021/9/15
 * Time: 15:02
 * Version:V1.0
 */
@Data
public class UploadDataDto {

    /**
     * 1: 初礼合同 firstGiveContract; 2: 一件代发合同 oneForDistributionContract;3:直播款-普通合同 liveCommonContract
     * 4: 直播款-预付合同 livePrepareContract; 5: 辅料合同 materialContract; 6: 除雪梨生活馆外的成衣合同 xueLiLiveOutContract;
     * 7: 雪梨生活馆 xueLiLiveContract;
     */
    private Integer templateId;

    /**
     * 采购单ID
     */
    private Integer poId;


    /**
     * 一件代发合同
     */
    private OneForDistributionContractDto oneForDistributionContractDto;

    /**
     * 初礼合同
     */
    private FirstGiveContractDto firstGiveContract;

    /**
     * 直播款-普通合同
     */
    private LiveCommonContractDto liveCommonContract;

    /**
     * 直播款-预发合同
     */
    private LivePrepareContractDto livePrepareContract;

    /**
     * 辅料合同
     */
    private MaterialContractDto materialContract;


    /**
     * 除雪梨生活馆外的成衣合同
     */
    private XueLiLiveOutContractDto xueLiLiveOutContract;

    /**
     * 雪梨生活馆
     */
    private XueLiLiveContractDto xueLiLiveContract;
}

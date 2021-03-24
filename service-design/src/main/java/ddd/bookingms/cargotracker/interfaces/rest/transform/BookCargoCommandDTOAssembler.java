package ddd.bookingms.cargotracker.interfaces.rest.transform;

import ddd.bookingms.cargotracker.domain.model.commands.BookCargoCommand;
import ddd.bookingms.cargotracker.interfaces.rest.dto.BookCargoResource;
import org.springframework.beans.BeanUtils;

/**
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:34
 * Version:V1.0
 */
public class BookCargoCommandDTOAssembler {

    /**
     * 这里是把界面的数据转换成内部想要的命令
     * @param bookCargoResource
     * @return
     */
    public static BookCargoCommand toCommandFromDTO(BookCargoResource bookCargoResource) {
        BookCargoCommand bookCargoCommand =new BookCargoCommand();
        BeanUtils.copyProperties(bookCargoResource, bookCargoCommand);
        return bookCargoCommand;
    }
}

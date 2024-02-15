package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.StatusListDAO;
import pl.Aevise.SupperSpeed.domain.StatusList;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatusListService {

    private final StatusListDAO statusListDAO;

    @Transactional
    public List<StatusList> getStatusList() {
        List<StatusList> statusList = statusListDAO.getStatusList();
        log.info("Found [{}] statuses", statusList.size());
        return statusList;

    }



}

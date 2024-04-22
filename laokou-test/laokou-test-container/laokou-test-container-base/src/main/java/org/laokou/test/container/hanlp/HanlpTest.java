package org.laokou.test.container.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class HanLPTest {
	public static void main(String[] args) {
		String str = "天使来了";
		List<Term> result = HanLP.segment(str);
		for (Term term : result) {
			log.info("{}", term.word);
		}
	}
}

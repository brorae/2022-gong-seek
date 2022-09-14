import { AxiosError, AxiosResponse } from 'axios';
import React, { useEffect, useState } from 'react';
import { useMutation } from 'react-query';

import { deleteLikeArticle, postAddLikeArticle } from '@/api/like';

const useHeartClick = ({
	prevIsLike,
	prevLikeCount,
	articleId,
}: {
	prevIsLike: boolean;
	prevLikeCount: number;
	articleId: string;
}) => {
	const [isLike, setIsLike] = useState(prevIsLike);
	const [likeCount, setLikeCount] = useState(prevLikeCount);
	const {
		mutate: postMutate,
		isError: postIsError,
		error: postError,
		isSuccess: postIsSuccess,
	} = useMutation<AxiosResponse, AxiosError, string>(`like${articleId}`, postAddLikeArticle, {
		retry: 1,
	});
	const {
		mutate: deleteMutate,
		isError: deleteIsError,
		error: deleteError,
		isSuccess: deleteIsSuccess,
	} = useMutation<AxiosResponse, AxiosError, string>(`unlike${articleId}`, deleteLikeArticle, {
		retry: 1,
	});

	useEffect(() => {
		setIsLike(prevIsLike);
		setLikeCount(prevLikeCount);
	}, [prevIsLike, prevLikeCount]);

	useEffect(() => {
		if (postIsError) {
			throw new Error(postError.message);
		}

		if (deleteIsError) {
			throw new Error(deleteError.message);
		}
	}, [postIsError, deleteIsError]);

	useEffect(() => {
		if (deleteIsSuccess) {
			setIsLike(false);
			setLikeCount((prevLikeCount) => prevLikeCount - 1);
		}
	}, [deleteIsSuccess]);

	useEffect(() => {
		if (postIsSuccess) {
			setIsLike(true);
			setLikeCount((prevLikeCount) => prevLikeCount + 1);
		}
	}, [postIsSuccess]);

	const onLikeButtonClick = (e: React.MouseEvent<SVGElement>) => {
		e.stopPropagation();
		postMutate(articleId);
	};

	const onUnlikeButtonClick = (e: React.MouseEvent<SVGElement>) => {
		e.stopPropagation();
		deleteMutate(articleId);
	};

	return {
		onLikeButtonClick,
		onUnlikeButtonClick,
		isLike,
		likeCount,
		postIsSuccess,
		deleteIsSuccess,
	};
};

export default useHeartClick;